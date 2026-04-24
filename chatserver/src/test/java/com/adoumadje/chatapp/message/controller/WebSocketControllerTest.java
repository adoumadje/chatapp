package com.adoumadje.chatapp.message.controller;

import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.enums.MessageType;
import com.adoumadje.chatapp.message.enums.MessageTarget;
import com.adoumadje.chatapp.message.service.IMessageService;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketControllerTest {
    @LocalServerPort
    private int localServerPort;
    @Value("${broker.prefix.handshake}")
    private String handshakePrefix;
    @Value("${broker.prefix.application-message}")
    private String applicationMessagePrefix;
    @Value("${broker.prefix.private-message}")
    private String privateMessagePrefix;
    @Value("${broker.prefix.group-message}")
    private String groupMessagePrefix;

    private UUID chatId;
    private UUID senderId;
    private UUID receiverId;
    private UUID groupId;

    private String CONNECT_URL;
    private String SEND_PRIVATE_MESSAGE;
    private String SEND_GROUP_MESSAGE;
    private String SUBSCRIBE_PRIVATE_MESSAGE;

    private CompletableFuture<MessageDto> completableFuture;
    private MessageDto messageDto;
    @MockitoBean
    private IMessageService messageService;

    @BeforeEach
    void setUp() {
        chatId = UUID.randomUUID();
        senderId = UUID.randomUUID();
        receiverId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        CONNECT_URL = "http://localhost:" + localServerPort + handshakePrefix;
        SEND_PRIVATE_MESSAGE = applicationMessagePrefix + "/private-message";
        SEND_GROUP_MESSAGE = applicationMessagePrefix + "/group-message";

        messageDto = createMessageDto();

        completableFuture = new CompletableFuture<>();
    }

    private MessageDto createMessageDto() {
        MessageDto msgDto = new MessageDto();
        msgDto.setMessageText("Hello there!");
        msgDto.setChatId(chatId);
        msgDto.setSenderId(senderId);
        msgDto.setReceiverId(receiverId);
        msgDto.setMessageType(MessageType.MESSAGE);
        return msgDto;
    }

    @DisplayName("Send Private Message - Sender")
    @Test
    void testHandlePrivateMessage_SenderSide_WhenSent_ReceiveWithTimeStamp() throws ExecutionException, InterruptedException, TimeoutException {
        SUBSCRIBE_PRIVATE_MESSAGE = privateMessagePrefix + "/" + senderId;

        Mockito.when(messageService.savePrivateMessage(Mockito.any(MessageDto.class))).thenReturn(createResponseMessageDto());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSession stompSession = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_PRIVATE_MESSAGE, new MessageStompFrameHandler());

        messageDto.setMessageTarget(MessageTarget.PRIVATE);
        stompSession.send(SEND_PRIVATE_MESSAGE, messageDto);

        MessageDto receivedMsg  = completableFuture.get(5, TimeUnit.SECONDS);

        Assertions.assertNotNull(receivedMsg.getTimeStamp());
        Assertions.assertEquals(MessageType.ACKNOWLEDGE, receivedMsg.getMessageType());
    }

    @DisplayName("Send Private Message - Receiver")
    @Test
    void testHandlePrivateMessage_ReceiverSide_WhenSent_ReceiveWithTimeStamp() throws ExecutionException, InterruptedException, TimeoutException {
        SUBSCRIBE_PRIVATE_MESSAGE = privateMessagePrefix + "/" + receiverId;

        Mockito.when(messageService.savePrivateMessage(Mockito.any(MessageDto.class))).thenReturn(createResponseMessageDto());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSession stompSessionReceiver = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);
        stompSessionReceiver.subscribe(SUBSCRIBE_PRIVATE_MESSAGE, new MessageStompFrameHandler());

        StompSession stompSessionSender = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);
        messageDto.setMessageTarget(MessageTarget.PRIVATE);
        stompSessionSender.send(SEND_PRIVATE_MESSAGE, messageDto);

        MessageDto receivedMsg  = completableFuture.get(5, TimeUnit.SECONDS);

        Assertions.assertNotNull(receivedMsg.getTimeStamp());
        Assertions.assertEquals(MessageType.MESSAGE, receivedMsg.getMessageType());
    }

    @DisplayName("Send Group Message")
    @Test
    void testHandleGroupMessage_WhenMessage_ThenTimeStamp() throws ExecutionException, InterruptedException, TimeoutException {
        String SUBSCRIBE_GROUP_MESSAGE = groupMessagePrefix + "/" + groupId;

        Mockito.when(messageService.saveGroupMessage(Mockito.any(MessageDto.class))).thenReturn(createResponseMessageDto());

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSession stompSession = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_GROUP_MESSAGE, new MessageStompFrameHandler());

        messageDto.setMessageTarget(MessageTarget.GROUP);
        messageDto.setReceiverId(groupId);
        stompSession.send(SEND_GROUP_MESSAGE, messageDto);

        MessageDto receivedMsg  = completableFuture.get(5, TimeUnit.SECONDS);

        Assertions.assertNotNull(receivedMsg.getTimeStamp());
    }

    private MessageDto createResponseMessageDto() {
        messageDto.setTimeStamp(LocalDateTime.now());
        return messageDto;
    }

    private List<Transport> createClientTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }



    private class StompSessionHandlerAdapterExt extends StompSessionHandlerAdapter {

    }

    private class MessageStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return MessageDto.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, @Nullable Object payload) {
            completableFuture.complete((MessageDto) payload);
        }
    }
}