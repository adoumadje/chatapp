package com.example.authserver.config;

import com.example.authserver.entity.AppUser;
import com.example.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Username: {}", username);
        AppUser dbUser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
        UserDetails userDetails = User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .authorities("READ", "WRITE")
                .roles("USER")
                .build();
        return userDetails;
    }
}
