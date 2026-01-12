package com.example.demo.filter;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationProviderTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JWTUtil jwtUtil;

    private JWTAuthenticationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new JWTAuthenticationProvider(userDetailsService, jwtUtil);
    }

    @Test
    void authenticate_success() {
        String token = "valid-token";
        when(jwtUtil.validateAndExtarctUserName(token)).thenReturn("alice");

        UserDetails user = User.withUsername("alice")
                .password("password")
                .roles("USER")
                .build();

        when(userDetailsService.loadUserByUsername("alice")).thenReturn(user);

        Authentication auth = provider.authenticate(new JwtAuthenticationToken(token));

        assertNotNull(auth);
        assertTrue(auth.isAuthenticated());
        assertEquals(user, auth.getPrincipal());
        assertEquals(token, auth.getCredentials());
        assertArrayEquals(user.getAuthorities().toArray(), auth.getAuthorities().toArray());
    }

    @Test
    void authenticate_invalidToken_throwsBadCredentials() {
        String token = "bad-token";
        when(jwtUtil.validateAndExtarctUserName(token)).thenThrow(new JwtException("invalid"));

        assertThrows(BadCredentialsException.class, () -> provider.authenticate(new JwtAuthenticationToken(token)));
    }

    @Test
    void authenticate_userNotFound_throwsBadCredentials() {
        String token = "valid-token";
        when(jwtUtil.validateAndExtarctUserName(token)).thenReturn("bob");
        when(userDetailsService.loadUserByUsername("bob")).thenThrow(new UsernameNotFoundException("not found"));

        assertThrows(BadCredentialsException.class, () -> provider.authenticate(new JwtAuthenticationToken(token)));
    }
}

