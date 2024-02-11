package com.cozyhome.onlineshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.cozyhome.onlineshop.userservice.security.jwt.JwtAuthEntryPoint;
import com.cozyhome.onlineshop.userservice.security.jwt.JwtTokenFilter;
import com.cozyhome.onlineshop.userservice.security.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;
	private final JwtAuthEntryPoint unauthorizedHandler;
	private final String logoutURL = "${api.secure.basePath}/user/logout";

	@Value("${api.auth.whitelist}")
	private final String[] AUTH_WHITELIST;

	@Bean
	public JwtTokenFilter authenticationJwtTokenFilter() {
		return new JwtTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated());

		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.cors(Customizer.withDefaults());
		http.oauth2Login(Customizer.withDefaults());
//		http.oauth2Client(Customizer.withDefaults());
//		http.oauth2ResourceServer(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public SecurityFilterChain filterLogoutChain(HttpSecurity http) throws Exception {
		http.logout(logout -> logout.logoutUrl(logoutURL).addLogoutHandler(new SecurityContextLogoutHandler()));
		return http.build();
	}
}