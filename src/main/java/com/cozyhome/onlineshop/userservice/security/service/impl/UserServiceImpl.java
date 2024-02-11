package com.cozyhome.onlineshop.userservice.security.service.impl;

import com.cozyhome.onlineshop.dto.auth.NewPasswordRequest;
import com.cozyhome.onlineshop.dto.auth.SignupRequest;
import com.cozyhome.onlineshop.dto.user.PasswordUpdateRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationResponse;
import com.cozyhome.onlineshop.exception.AuthException;
import com.cozyhome.onlineshop.exception.DataAlreadyExistException;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.exception.InvalidTokenException;
import com.cozyhome.onlineshop.userservice.model.Role;
import com.cozyhome.onlineshop.userservice.model.RoleE;
import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.model.UserStatusE;
import com.cozyhome.onlineshop.userservice.model.token.PasswordResetToken;
import com.cozyhome.onlineshop.userservice.model.token.SecurityToken;
import com.cozyhome.onlineshop.userservice.repository.RoleRepository;
import com.cozyhome.onlineshop.userservice.repository.SecurityTokenRepository;
import com.cozyhome.onlineshop.userservice.repository.UserRepository;
import com.cozyhome.onlineshop.userservice.security.service.SecurityTokenService;
import com.cozyhome.onlineshop.userservice.security.service.UserService;
import com.cozyhome.onlineshop.userservice.security.service.builder.UserBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;
	private final SecurityTokenRepository securityTokenRepository;
	private final SecurityTokenService securityTokenService;
	private final UserBuilder userBuilder;

	private final String admin = "admin";
	private final String manager = "manager";
	private final String roleErrorMessage = "Error: Role is not found.";

	@Override
	public void saveUser(SignupRequest signupRequest) {
		User user = User.builder()
				.email(signupRequest.getEmail())
				.password(encoder.encode(signupRequest.getPassword()))
				.firstName(signupRequest.getFirstName())
				.lastName(signupRequest.getLastName())
				.phoneNumber(signupRequest.getPhoneNumber())
				.createdAt(LocalDateTime.now())
				.status(UserStatusE.ACTIVE)
				.build();

		if (signupRequest.getBirthday() != null && !signupRequest.getBirthday().isEmpty()) {
			LocalDate birthday = LocalDate.parse(signupRequest.getBirthday());
			user.setBirthday(birthday);
		}

		Set<String> userRoles = signupRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (userRoles == null) {
			Role userRole = roleRepository.getByName(RoleE.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException(roleErrorMessage));
			roles.add(userRole);
		} else {
			userRoles.forEach(role -> {
				switch (role) {
				case admin:
					Role adminRole = roleRepository.getByName(RoleE.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(roleErrorMessage));
					roles.add(adminRole);
					break;
				case manager:
					Role managerRole = roleRepository.getByName(RoleE.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException(roleErrorMessage));
					roles.add(managerRole);
					break;
				default:
					Role userRole = roleRepository.getByName(RoleE.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException(roleErrorMessage));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		User savedUser = userRepository.save(user);
		securityTokenService.createActivationUserToken(savedUser);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmailAndStatus(email, UserStatusE.ACTIVE);
	}

	@Override
	public User activateUser(String token) {
		SecurityToken activationToken = securityTokenRepository.findByToken(token);

        if (activationToken == null || activationToken.isExpired()) {
            throw new InvalidTokenException("Invalid or expired activation token");
        }

		User user = activationToken.getUser();
		user.setActivated(true);
		userRepository.save(user);

		securityTokenRepository.delete(activationToken);

		return user;
	}

	@Override
	public User resetPassword(String token, NewPasswordRequest newPassword) {
		PasswordResetToken resetToken = (PasswordResetToken) securityTokenRepository.findByToken(token);

        if (resetToken == null || resetToken.isExpired()) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(encoder.encode(newPassword.getPassword()));
		user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);

        securityTokenRepository.delete(resetToken);	
        return user;
	}

    @Override
    public void updateUserPassword(PasswordUpdateRequest passwords, String userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatusE.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException("Unable to update password. User not found."));
        if (encoder.matches(passwords.getOldPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(passwords.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new AuthException("Wrong old password entered.");
        }
    }

    @Override
    public UserInformationResponse updateUserData(UserInformationRequest userInformationDto, String userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatusE.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("User with email = %s not found.", userInformationDto.getEmail())));

		user.setLastName(userInformationDto.getLastName());
		user.setFirstName(userInformationDto.getFirstName());
		user.setPhoneNumber(userInformationDto.getPhoneNumber());
		user.setModifiedAt(LocalDateTime.now());

        if (!user.getEmail().equals(userInformationDto.getEmail())) {
            if (userRepository.existsByEmailAndStatus(userInformationDto.getEmail(), UserStatusE.ACTIVE)) {
                throw new DataAlreadyExistException(String.format("Email %s is already in use", userInformationDto.getEmail()));
            }
            user.setEmail(userInformationDto.getEmail());
        }

		if (!userInformationDto.getBirthday().isEmpty()) {
			LocalDate birthday = LocalDate.parse(userInformationDto.getBirthday());
			user.setBirthday(birthday);
		}

        User updatedUser = userRepository.save(user);
        return userBuilder.buildUserInformationResponse(updatedUser);
    }

	@Override
	public UserInformationResponse getUserInfo(String userId) {
		User user = userRepository.findByIdAndStatus(userId, UserStatusE.ACTIVE)
				.orElseThrow(() -> new DataNotFoundException(String.format("User with id = %s not found.", userId)));
		return userBuilder.buildUserInformationResponse(user);
	}

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmailAndStatus(email, UserStatusE.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException("Not user found by the email " + email));
        user.setModifiedAt(LocalDateTime.now());
        user.setStatus(UserStatusE.DELETED);
        log.info("[ON deleteUser] :: changed the user status to DELETED for the user with email {}", email);
        userRepository.save(user);
    }
}
