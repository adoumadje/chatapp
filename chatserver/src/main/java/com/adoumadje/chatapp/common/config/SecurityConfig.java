package com.adoumadje.chatapp.common.config;

import com.adoumadje.chatapp.common.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver = JwtIssuerAuthenticationManagerResolver
                .fromTrustedIssuers(Constants.GOOGLE_TOKEN_ISSUER, Constants.AUTH_SERVER_TOKEN_ISSUER);

        http.sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.csrf(csrfConfig -> csrfConfig
                .ignoringRequestMatchers("/api/v1/users"));

        http.authorizeHttpRequests(requestConfig -> requestConfig
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                .anyRequest().authenticated());

        http.oauth2ResourceServer(rsConfig -> rsConfig
                .authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
