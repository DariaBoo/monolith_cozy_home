package com.cozyhome.onlineshop.userservice.controller;

import com.cozyhome.onlineshop.dto.auth.EmailRequest;
import com.cozyhome.onlineshop.dto.auth.LoginRequest;
import com.cozyhome.onlineshop.dto.auth.MessageResponse;
import com.cozyhome.onlineshop.dto.auth.NewPasswordRequest;
import com.cozyhome.onlineshop.dto.auth.SignupRequest;
import com.cozyhome.onlineshop.exception.AuthenticationException;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.security.JWT.JwtTokenUtil;
import com.cozyhome.onlineshop.userservice.security.service.SecurityService;
import com.cozyhome.onlineshop.userservice.security.service.SecurityTokenService;
import com.cozyhome.onlineshop.userservice.security.service.UserService;

import com.cozyhome.onlineshop.validation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}" })
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

	private final String emailErrorMessage = "Error: Email is already in use!";
	private final String registrationSuccessMessage = "User registered successfully!";

	@Operation(summary = "Existing user login", description = "Existing user login by email and password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		boolean isAuthenticated = securityService.isAuthenticated(username, loginRequest.getPassword());
		if (isAuthenticated) {
			String token = jwtTokenUtil.generateToken(username);
			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
		} else {
			log.warn("[ON login]:: Authentication failed for user: {}", username);
			throw new AuthenticationException("Authentication failed for user");
		}
	}

	@Operation(summary = "New user registration", description = "Registering new user and sending e-mail with a link to activate e-mail.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid SignupRequest signUpRequest) {
		String email = signUpRequest.getEmail();
		if (userService.existsByEmail(email)) {
			return ResponseEntity.badRequest().body(new MessageResponse(emailErrorMessage));
		}
		User savedUser = userService.saveUser(signUpRequest);
		securityTokenService.createActivationUserToken(savedUser);
		return ResponseEntity.ok(new MessageResponse(registrationSuccessMessage));
	}

	@Operation(summary = "Activate e-mail.", description = "User follows the link sent to him during registration and activates his mail in this method")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping("/activate")
	public ResponseEntity<MessageResponse> activateUser(@RequestParam @ValidUUID String activationToken) {
		User activatedUser = userService.activateUser(activationToken);
		String token = jwtTokenUtil.generateToken(activatedUser.getEmail());
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, token).body(new MessageResponse("success"));
	}

	@Operation(summary = "Logout.", description = "Logout.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails != null) {
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
			return ResponseEntity.ok("Logout successful");
		} else {
			return ResponseEntity.ok("No user is logged in");
		}
	}

	@Operation(summary = "User is sent a link to change his password",
			description = "If user doesn't remember his password during logging, link to change his password is sent to his e-mail.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login/forgot")
	public ResponseEntity<MessageResponse> forgetPassword(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest httpRequest) {
		securityTokenService.createPasswordResetToken(emailRequest.getEmail(), httpRequest.getRemoteAddr());
		return ResponseEntity.ok(new MessageResponse("success"));
	}

	@Operation(summary = "User enters new password",
			description = "User follows the link previously sent to his e-mail and enters new password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@PostMapping("/login/reset")
	public ResponseEntity<MessageResponse> resetPassword(@RequestParam @ValidUUID String resetPasswordToken, @RequestBody @Valid NewPasswordRequest newPassword) {
		User user = userService.resetPassword(resetPasswordToken, newPassword);
		String token = jwtTokenUtil.generateToken(user.getEmail());
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
	}

}
