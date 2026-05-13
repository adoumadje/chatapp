package com.example.authserver.handler;

import com.adoumadje.chatapp.core.events.UserRegisteredEvent;
import com.example.authserver.UserRepository;
import com.example.authserver.entity.AppUser;
import com.example.authserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "user-events")
@RequiredArgsConstructor
public class UserRegisteredEventHandler {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    @KafkaHandler
    public void handleUserRegisteredEvent(@Payload UserRegisteredEvent userRegisteredEvent) {
        AppUser appUser = userMapper.toAppUser(userRegisteredEvent);
        userRepository.save(appUser);
        logger.info("User successfully registered in AuthServer");
    }
}
