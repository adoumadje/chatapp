package com.adoumadje.chatapp.message.controller;

import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.enums.MessageTarget;
import com.adoumadje.chatapp.message.enums.MessageType;
import com.adoumadje.chatapp.message.exception.WrongMessageTargetException;
import com.adoumadje.chatapp.message.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final IMessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${broker.prefix.private-message}")
    private String privateMessagePrefix;
    @Value("${broker.prefix.group-message}")
    private String groupMessagePrefix;

    private final static Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload @Validated MessageDto messageDto) {
        if(messageDto.getMessageTarget() != MessageTarget.PRIVATE) {
            throw new WrongMessageTargetException("Message is not a private message");
        }
        MessageDto savedMessageDto = messageService.savePrivateMessage(messageDto);
        simpMessagingTemplate.convertAndSend(privateMessagePrefix + "/" + messageDto.getReceiverId(),
                savedMessageDto);
        savedMessageDto.setMessageType(MessageType.ACKNOWLEDGE);
        simpMessagingTemplate.convertAndSend(privateMessagePrefix + "/" + messageDto.getSenderId(),
                savedMessageDto);
    }

    @MessageMapping("/group-message")
    public void handleGroupMessage(@Payload @Validated MessageDto messageDto) {
        if (messageDto.getMessageTarget() != MessageTarget.GROUP) {
            throw new WrongMessageTargetException("Message is not a group message");
        }
        MessageDto savedMessageDto = messageService.saveGroupMessage(messageDto);
        simpMessagingTemplate.convertAndSend(groupMessagePrefix + "/" + messageDto.getReceiverId(),
                savedMessageDto);
    }
}
