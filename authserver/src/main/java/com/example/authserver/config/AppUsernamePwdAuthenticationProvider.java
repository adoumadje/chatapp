package com.example.authserver.config;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AppUsernamePwdAuthenticationProvider implements AuthenticationProvider {
    private final AppUserDetailsService appUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = Objects.requireNonNull(authentication.getCredentials()).toString();
        UserDetails dbUser = appUserDetailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        Collection<GrantedAuthority> grantedAuthorities = List.of(FactorGrantedAuthority
                .fromAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY));
        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
