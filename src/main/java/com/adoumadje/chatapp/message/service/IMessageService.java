package com.adoumadje.chatapp.message.service;

import com.adoumadje.chatapp.message.dto.MessageDto;

public interface IMessageService {

    MessageDto savePrivateMessage(MessageDto messageDto);

    MessageDto saveGroupMessage(MessageDto messageDto);
}
