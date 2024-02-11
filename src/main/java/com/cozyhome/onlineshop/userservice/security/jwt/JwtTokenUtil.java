package com.cozyhome.onlineshop.userservice.security.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.exception.InvalidTokenException;
import com.cozyhome.onlineshop.userservice.repository.TokenBlackListRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	private final TokenBlackListRepository tokenBlackListRepository;

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.token.validity}")
	private int tokenValiditi;
	private final static String TOKEN_PREFIX = "Bearer ";

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(String username) {
		String token = Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + tokenValiditi))
				.signWith(key(), SignatureAlgorithm.HS256)
				.compact();
		log.info("[ON generateToken]:: token has generated successfully");
		return token;
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		log.info("[ON validateToken]:: token validation...");
		final String username = getUsernameFromToken(token);
		boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		log.info("[ON validateToken]:: is token valid - {}", isValid);
		return isValid;
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION);
		if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(TOKEN_PREFIX.length());
		}
		return null;
	}

	public boolean isTokenInBlackList(String jwtToken) {
		return tokenBlackListRepository.existsById(jwtToken);
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	private Claims getAllClaimsFromToken(String token) {
		log.info("[ON getAllClaimsFromToken]:: getting all claims from token using Jwts parser");
		Claims claims;
		try {
			claims = Jwts.parserBuilder()
					.setSigningKey(key())
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (JwtException e) {
			log.error("[ON getAllClaimsFromToken]:: token is expired");
			throw new InvalidTokenException("Token is invalid.");
		}
		return claims;
	}

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		boolean isExpired = expiration.before(new Date());
		log.info("[ON isTokenExpired]:: is token expired - {}", isExpired);
		return isExpired;
	}
}
