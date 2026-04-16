package com.adoumadje.chatapp.user.service.implementation;

import com.adoumadje.chatapp.common.exception.ResourceNotFoundException;
import com.adoumadje.chatapp.user.UserRepository;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import com.adoumadje.chatapp.user.service.IUserService;
import com.adoumadje.chatapp.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findUsers(Principal principal, String keyword, Integer pageNumber) {
        pageNumber = pageNumber == null ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE);
        String email = principal.getName();
        Optional<ChatUser> optionalChatUser = userRepository.findByEmail(email);
        if(optionalChatUser.isEmpty()) {
            throw new ResourceNotFoundException(ChatUser.class.getSimpleName(), "email", email);
        }
        ChatUser user = optionalChatUser.get();
        return keyword == null ? findUsers(user, pageable) : findUsers(user, keyword, pageable);
    }

    private List<UserDto> findUsers(ChatUser user, Pageable pageable) {
        Page<ChatUser> chatUsersPage = userRepository.findByIdNot(user.getId(), pageable);
        List<ChatUser> chatUsers = chatUsersPage.getContent();
        return null;
    }

    private List<UserDto> findUsers(ChatUser user, String keyword, Pageable page) {
        return null;
    }
}
