package com.example.demo.filter;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private JWTUtil jwtUtil;

    public JWTAuthenticationProvider(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new BadCredentialsException("Unsupported authentication token");
        }

        String token = ((JwtAuthenticationToken) authentication).getToken();
        String username;

        try {
            username = jwtUtil.validateAndExtarctUserName(token);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid JWT token", ex);
        }

        if (username == null || username.isBlank()) {
            throw new BadCredentialsException("JWT token does not contain a subject");
        }

        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(username);
        } catch (Exception ex) {
            throw new BadCredentialsException("User not found for JWT subject", ex);
        }

        // return an authenticated JwtAuthenticationToken containing the UserDetails and authorities
        return new JwtAuthenticationToken(user, token, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
