package com.example.authserver.config;

import com.example.authserver.entity.AppUser;
import com.example.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser dbUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
        UserDetails userDetails = User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles("USER")
                .build();
        return userDetails;
    }
}
