package com.cozyhome.onlineshop.userservice.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cozyhome.onlineshop.dto.auth.CustomSignupRequest;
import com.cozyhome.onlineshop.dto.auth.EmailRequest;
import com.cozyhome.onlineshop.dto.auth.LoginRequest;
import com.cozyhome.onlineshop.dto.auth.MessageResponse;
import com.cozyhome.onlineshop.dto.auth.NewPasswordRequest;
import com.cozyhome.onlineshop.dto.auth.SignupRequest;
import com.cozyhome.onlineshop.dto.auth.TokenResponse;
import com.cozyhome.onlineshop.exception.AuthException;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.security.jwt.JwtTokenUtil;
import com.cozyhome.onlineshop.userservice.security.service.SecurityService;
import com.cozyhome.onlineshop.userservice.security.service.SecurityTokenService;
import com.cozyhome.onlineshop.userservice.security.service.UserService;
import com.cozyhome.onlineshop.validation.ValidUUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}" })
@Tag(name = "Auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("${api.basePath}/auth")
public class AuthController {

	private final UserService userService;
	private final SecurityService securityService;
	private final JwtTokenUtil jwtTokenUtil;
	private final SecurityTokenService securityTokenService;
	
	@Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
	private String userInfoEndpoint;
	
	@Value("${spring.security.oauth2.client.provider.google.token-uri}")
	private String tokenEndpoint;
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String client_id;
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String client_secret;
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirect_uri;
	@Value("${spring.security.oauth2.client.proveder.google.grant-type}")
	private String grant_type ;
	
	private final String emailErrorMessage = "Error: Email is already in use!";

	@Operation(summary = "Existing user login", description = "Allows an existing user to log in using his email and password.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		boolean isAuthenticated = securityService.isAuthenticated(username, loginRequest.getPassword());
		boolean isActivated = securityService.isActivated(username);
		log.info("[ON login] :: User with email {} is active - {}", loginRequest.getEmail(), isActivated);
		if (isAuthenticated && isActivated) {
			String token = jwtTokenUtil.generateToken(username);
			return ResponseEntity.ok().body(new TokenResponse(token));
		} else {
			log.warn("[ON login]:: Authentication failed for user: {}", username);
			throw new AuthException("Authentication failed for user - " + username);
		}
	}

	@GetMapping("/google-login")
	public ResponseEntity<Void> getGoogleCode() {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/google-login")
	public ResponseEntity<TokenResponse> googleLogin(@RequestParam("code") String code) {
		String jwtToken = "";
		try {
			Optional<String> accessToken = getGoogleAccessToken(code);
			JsonNode userInfoNode = null;
			if (accessToken.isPresent()) {
				userInfoNode = recieveUserInfoFromAccessToken(accessToken.get());
			}
			if (userInfoNode != null) {
				String userEmail = userInfoNode.get("email").asText();
				if (userEmail != null) {
					boolean isUserExists = userService.existsByEmail(userEmail);
					if (!isUserExists) {
						Optional<String> name = Optional.ofNullable(userInfoNode.get("given_name").asText());
						SignupRequest signupRequest = new SignupRequest();
						signupRequest.setEmail(userEmail);
						if(name.isPresent()) {
						signupRequest.setFirstName(name.get());
						}
						userService.saveUser(signupRequest);
					} 
					jwtToken = jwtTokenUtil.generateToken(userEmail);
				}
			}
			return ResponseEntity.ok().body(new TokenResponse(jwtToken));
		} catch (JsonProcessingException e) {
			log.error("Json processing exception: " + e.getMessage());
			return ResponseEntity.badRequest().build();
		}

	}

	private JsonNode recieveUserInfoFromAccessToken(String accessToken)
			throws JsonMappingException, JsonProcessingException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(userInfoEndpoint));
		ResponseEntity<String> response = new RestTemplate().exchange(request, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode userInfoNode = objectMapper.readTree(response.getBody());
		return userInfoNode;
	}

	private Optional<String> getGoogleAccessToken(String code) throws JsonMappingException, JsonProcessingException {

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("code", code);
		requestBody.add("client_id", client_id);
		requestBody.add("client_secret", client_secret);
		requestBody.add("redirect_uri", redirect_uri);
		requestBody.add("grant_type", grant_type);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<String> responseEntity = new RestTemplate().postForEntity(tokenEndpoint, requestEntity,
					String.class);
			String responseBody = responseEntity.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			String accessToken = jsonNode.get("access_token").asText();
			return Optional.of(accessToken);
		} catch (HttpClientErrorException e) {
			log.error("GOOGLE ERROR RESPONSE: " + e.getResponseBodyAsString());
			throw e;
		}
	}

	@Operation(summary = "New user registration", description = "Registers a new user and sends an email with a link to activate his account.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid CustomSignupRequest signUpRequest) {
		String email = signUpRequest.getEmail();
		if (userService.existsByEmail(email)) {
			log.error("[ON registerUser] :: email is already in use.");
			return ResponseEntity.badRequest().body(new MessageResponse(emailErrorMessage));
		}
		userService.saveUser(signUpRequest);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Activate e-mail.", description = "User follows the link sent to him during registration and activates his mail in this method")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping("/activate")
	public ResponseEntity<TokenResponse> activateUser(@RequestParam @ValidUUID String activationToken) {
		User activatedUser = userService.activateUser(activationToken);
		String token = jwtTokenUtil.generateToken(activatedUser.getEmail());
		return ResponseEntity.ok().body(new TokenResponse(token));
	}

	@Operation(summary = "Send a password reset link to the user's email address", description = "Sends a password reset link to the user's email address if the user have forgotten his password.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login/forgot")
	public ResponseEntity<Void> forgetPassword(@RequestBody @Valid EmailRequest emailRequest,
			HttpServletRequest httpRequest) {
		securityTokenService.createPasswordResetToken(emailRequest.getEmail(), httpRequest.getRemoteAddr());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Reset user password", description = "User follows the link previously sent to his e-mail and enters a new password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login/reset")
	public ResponseEntity<TokenResponse> resetPassword(@RequestParam @ValidUUID String resetPasswordToken,
			@RequestBody @Valid NewPasswordRequest newPassword) {
		User user = userService.resetPassword(resetPasswordToken, newPassword);
		String token = jwtTokenUtil.generateToken(user.getEmail());
		return ResponseEntity.ok().body(new TokenResponse(token));
	}

	@Operation(summary = "Delete user.", description = "Delete user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/delete-account")
	public ResponseEntity<String> deleteUser(@RequestBody @Valid EmailRequest emailRequest) {
		userService.deleteUser(emailRequest.getEmail());
		log.info("[ON deleteUser] :: user deleted successfully!");
		return ResponseEntity.ok().build();
	}
}
