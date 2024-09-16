package com.gabrielgua.realworld.api.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token;
        final String email;
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("AUTH-HEADER: "+ authHeader);
        System.out.println("AUTH-HEADER: "+ authHeader);
        if (authHeader == null || !authHeader.startsWith("Token ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(6);
        logger.info("AUTH-TOKEN: "+ token);
        email = tokenService.extractEmail(token);
        logger.info("AUTH-EMAIL: "+ email);
        if (email != null && !isAuthenticated()) {
            var userDetails = userDetailsService.loadUserByUsername(email);

            if (tokenService.isTokenValid(token, userDetails.getUsername())) {
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request, response);

    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
}
