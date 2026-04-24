package com.adoumadje.chatapp.message.service.implementation;

import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.service.IMessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService {

    @Override
    public MessageDto savePrivateMessage(MessageDto messageDto) {
        return null;
    }

    @Override
    public MessageDto saveGroupMessage(MessageDto messageDto) {
        return null;
    }
}
