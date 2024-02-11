package com.cozyhome.onlineshop.userservice.security.jwt;

import java.io.IOException;

import com.cozyhome.onlineshop.exception.AuthException;
import com.cozyhome.onlineshop.userservice.security.AuthenticatedUserDetails;
import com.cozyhome.onlineshop.userservice.security.service.ExtendedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ExtendedUserDetailsService userDetailsService;
    @Value("${header.name.user-id}")
    private String userIdAttributeName;
    @Value("${header.name.user-role}")
    private String userRoleAttributeName;
    @Value("${api.secure.basePath}")
    private String authPathUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = jwtTokenUtil.resolveToken(request);
        String username = null;
        if (shouldFilter(request) || jwtToken != null) {
            log.info("[ON doFilterInternal]:: jwtToken [{}]", jwtToken);
            if (jwtTokenUtil.isTokenInBlackList(jwtToken)) {
                log.warn("[ON doFilterInternal]:: Token is in Black List. Access denied");
                throw new AuthException("User logged out. Access denied.");
            }
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            log.info("[ON doFilterInternal]:: username [ {} ]", username);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("[ON doFilterInternal]:: starting token validation...");
            AuthenticatedUserDetails user = userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.isTokenValid(jwtToken, user)) {
                request.setAttribute(userIdAttributeName, user.getUser().getId());
                request.setAttribute(userRoleAttributeName, user.getUser().getRoles());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[ON doFilterInternal]:: set authentication to SecurityContextHolder - {}", authToken);
            }
        }
        log.info("[ON doFilterInternal]:: filtering request and response by FilterChain");
        filterChain.doFilter(request, response);
    }

    private boolean shouldFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return requestUri.toLowerCase().startsWith(authPathUrl);
    }
}