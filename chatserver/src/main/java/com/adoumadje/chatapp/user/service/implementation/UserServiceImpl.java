package com.adoumadje.chatapp.user.service.implementation;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.common.exception.ResourceAlreadyExistsException;
import com.adoumadje.chatapp.common.exception.ResourceNotFoundException;
import com.adoumadje.chatapp.core.events.UserRegisteredEvent;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import com.adoumadje.chatapp.user.repository.UserRepository;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import com.adoumadje.chatapp.user.mapper.UserMapper;
import com.adoumadje.chatapp.user.service.IUserService;
import com.adoumadje.chatapp.common.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${user.events.topic.name}")
    private String userEventsTopicName;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getOrCreateUser(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuthenticationToken.getToken();
        String issuer = (String) jwt.getClaims().get("iss");
        if(issuer.equals(Constants.GOOGLE_TOKEN_ISSUER)) {
            return googleGetOrCreateUser(jwt);
        } else if (issuer.equals(Constants.AUTH_SERVER_TOKEN_ISSUER)) {
            return getLocalUser(jwt);
        } else {
            throw new RuntimeException("Unknown token issuer");
        }
    }


    @Override
    public List<UserDto> findUsers(Authentication authentication, String keyword, Integer pageNumber) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuthenticationToken.getToken();
        pageNumber = pageNumber == null ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE);
        String email = jwt.getClaims().get("email").toString();
        Optional<ChatUser> optionalChatUser = userRepository.findByEmail(email);
        if(optionalChatUser.isEmpty()) {
            throw new ResourceNotFoundException(ChatUser.class.getSimpleName(), "email", email);
        }
        ChatUser user = optionalChatUser.get();
        List<ChatUser> chatUsers = keyword == null ? findUsers(user, pageable) : findUsers(user, keyword, pageable);
        return userMapper.toDtoList(chatUsers);
    }

    @Override
    public ResponseDto registerUser(UserRegistrationDto userRegistrationDto) {
        Optional<ChatUser> optionalChatUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if(optionalChatUser.isPresent()) {
            throw new ResourceAlreadyExistsException(ChatUser.class.getSimpleName(), "email",
                    userRegistrationDto.getEmail());
        }
        UUID mailBoxId = UUID.randomUUID();
        userRegistrationDto.setUsername(userRegistrationDto.getUsername() + "__" + mailBoxId);
        String hashedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        userRegistrationDto.setPassword(hashedPassword);
        ChatUser chatUser = userMapper.toChatUser(userRegistrationDto);
        chatUser.setMailBoxId(mailBoxId);
        userRepository.save(chatUser);
        UserRegisteredEvent userRegisteredEvent = userMapper.toUserRegisteredEvent(userRegistrationDto);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(userEventsTopicName, userRegisteredEvent);
        future.whenComplete((result, exception) -> {
            if(exception == null) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                logger.info("Event Sent with: {partition: {}, offset: {}}", recordMetadata.partition(),
                        recordMetadata.offset());
            } else {
                throw new RuntimeException(exception.getMessage());
            }
        });
        future.join();
        return new ResponseDto(Constants.STATUS_ACCEPTED, Constants.USER_REGISTRATION_MSG);
    }

    private List<ChatUser> findUsers(ChatUser user, Pageable pageable) {
        Page<ChatUser> chatUserPage = userRepository.findByIdNot(user.getId(), pageable);
        return chatUserPage.getContent();
    }

    private List<ChatUser> findUsers(ChatUser user, String keyword, Pageable pageable) {
        Page<ChatUser> chatUserPage = userRepository.searchUsers(user.getId(), keyword, pageable);
        return chatUserPage.getContent();
    }

    private UserDto getLocalUser(Jwt jwt) {
        String email = jwt.getClaims().get("email").toString();
        Optional<ChatUser> optionalChatUser = userRepository.findByEmail(email);
        if(optionalChatUser.isEmpty()) {
            throw new ResourceNotFoundException(ChatUser.class.getSimpleName(), "email", email);
        }
        ChatUser chatUser = optionalChatUser.get();
        return userMapper.toDto(chatUser);
    }

    private UserDto googleGetOrCreateUser(Jwt jwt) {
        String email = jwt.getClaims().get("email").toString();
        Optional<ChatUser> optionalChatUser = userRepository.findByEmail(email);
        if(optionalChatUser.isEmpty()) {
            return registerGoogleUser(jwt);
        }
        ChatUser chatUser = optionalChatUser.get();
        return userMapper.toDto(chatUser);
    }

    private UserDto registerGoogleUser(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        UUID mailBoxId = UUID.randomUUID();
        String username = "GoogleUser__" + claims.get("sub") + "__" + mailBoxId;
        String firstname = claims.get("given_name").toString();
        String lastname = claims.get("family_name").toString();
        String email = claims.get("email").toString();
        String profilePictureUrl = claims.get("picture").toString();
        ChatUser chatUser = ChatUser.builder().username(username)
                .firstname(firstname).lastname(lastname).email(email)
                .profilePictureUrl(profilePictureUrl).mailBoxId(mailBoxId).build();
        ChatUser savedChatUser = userRepository.save(chatUser);
        return userMapper.toDto(savedChatUser);
    }
}
