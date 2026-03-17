package com.adoumadje.chatapp.message.controller;

import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.enums.MessageType;
import com.adoumadje.chatapp.message.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final IMessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final static Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload MessageDto messageDto) {
        if(messageDto.getMessageType() != MessageType.PRIVATE) {
            throw new RuntimeException("Wrong Destination");
        }
        messageService.saveMessage(messageDto);
        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverId().toString(),
                "/user", messageDto);
    }
}
