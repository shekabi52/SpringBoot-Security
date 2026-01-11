package com.example.demo;

import com.example.demo.filter.JWTAuthenticationProvider;
import com.example.demo.filter.JWTCustomFilter;
import com.example.demo.filter.JWTUtil;
import com.example.demo.filter.JWTValidationUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@Configuration
public class WebConfig {

    @Bean
    public DefaultSecurityFilterChain alternateFilter(HttpSecurity http,
                                                      AuthenticationManager authenticationManager,
                                                      JWTUtil jwtUtil) throws Exception {
        // instantiate filters directly to avoid proxying and logger initialization issues
        JWTCustomFilter jwtCustomFilter = new JWTCustomFilter(authenticationManager, jwtUtil);
        JWTValidationUtils jwtValidationUtils = new JWTValidationUtils(authenticationManager);

        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/welcome").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCustomFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationUtils, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(UserDetailsService userDetailsService,
                                                               JWTUtil jwtUtil) {
        return new JWTAuthenticationProvider(userDetailsService, jwtUtil);
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
