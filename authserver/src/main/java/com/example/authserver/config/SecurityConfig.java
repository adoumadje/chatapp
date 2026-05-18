package com.example.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrfConfig -> csrfConfig
                        .ignoringRequestMatchers("/h2-console/**"))
                .headers(headersConfig -> headersConfig
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(Customizer.withDefaults())
                .oauth2AuthorizationServer((authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
                );
        return http.build();
    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(UserRepository userRepository) {
//        return context -> {
//            if(context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
//                context.getClaims().claims(claims -> {
//                    String username = context.getPrincipal().getName();
//                    AppUser appUser = userRepository.findByUsername(username).orElseThrow();
//                    claims.put("email", appUser.getEmail());
//                });
//            }
//        };
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}