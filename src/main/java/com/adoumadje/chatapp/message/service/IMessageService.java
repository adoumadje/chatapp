package com.adoumadje.chatapp.message.service;

import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.entity.Message;

public interface IMessageService {
    Message saveMessage(MessageDto messageDto);
}
