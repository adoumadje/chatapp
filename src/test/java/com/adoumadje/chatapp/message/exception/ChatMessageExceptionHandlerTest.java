package com.adoumadje.chatapp.message.exception;

import com.adoumadje.chatapp.message.dto.ErrorMessageDto;
import com.adoumadje.chatapp.message.dto.MessageDto;
import com.adoumadje.chatapp.message.dto.ValidationErrorsMessageDto;
import com.adoumadje.chatapp.message.enums.MessageTarget;
import com.adoumadje.chatapp.message.enums.MessageType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatMessageExceptionHandlerTest {
    @LocalServerPort
    private int localServerPort;
    @Value("${broker.prefix.handshake}")
    private String handshakePrefix;
    @Value("${broker.prefix.application-message}")
    private String applicationMessagePrefix;
    @Value("${broker.prefix.error-message}")
    private String errorMessagePrefix;

    private UUID chatId;
    private UUID senderId;
    private UUID receiverId;
    private UUID groupId;

    private String CONNECT_URL;
    private String SEND_PRIVATE_MESSAGE;
    private String SEND_GROUP_MESSAGE;
    private String SUBSCRIBE_ERROR_MESSAGE;

    private CompletableFuture<ValidationErrorsMessageDto> validationErrorsMessageDtoCompletableFuture;
    private CompletableFuture<ErrorMessageDto> errorMessageDtoCompletableFuture;
    private MessageDto messageDto;

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

        validationErrorsMessageDtoCompletableFuture = new CompletableFuture<>();
        errorMessageDtoCompletableFuture = new CompletableFuture<>();
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

    @DisplayName("Validation for DTO Fields")
    @Test
    void testHandleMessageDtoValidationException_WhenBadPayload_ReturnValidationErrorsMessage()throws Exception {
        SUBSCRIBE_ERROR_MESSAGE = errorMessagePrefix + "/default";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSession stompSession = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_ERROR_MESSAGE, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ValidationErrorsMessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, @Nullable Object payload) {
                validationErrorsMessageDtoCompletableFuture.complete((ValidationErrorsMessageDto) payload);
            }
        });

        messageDto.setSenderId(null);
        messageDto.setMessageTarget(MessageTarget.PRIVATE);
        stompSession.send(SEND_PRIVATE_MESSAGE, messageDto);

        ValidationErrorsMessageDto receivedMsg  = validationErrorsMessageDtoCompletableFuture.get(5, TimeUnit.SECONDS);
        Assertions.assertEquals(MessageType.VALIDATION_ERRORS, receivedMsg.messageType());
        Assertions.assertNotNull(receivedMsg.validationErrors());
        Assertions.assertFalse(receivedMsg.validationErrors().isEmpty());
    }

    @DisplayName("When wrong JSON")
    @Test
    void testHandleMessageConversionException_WhenWrongJSON_SendErrorMessage() throws Exception {
        SUBSCRIBE_ERROR_MESSAGE = errorMessagePrefix + "/default";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSession stompSession = stompClient.connectAsync(CONNECT_URL, new StompSessionHandlerAdapterExt())
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_ERROR_MESSAGE, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ErrorMessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, @Nullable Object payload) {
                errorMessageDtoCompletableFuture.complete((ErrorMessageDto) payload);
            }
        });

        String messageJSON = """
                {
                    "messageType": "MESSAGE",
                    "messageTarget": "PRIVATE",
                    "messageText": "Hello world!",
                    "messageImage": "",
                    "senderId": "adac7355-973e-44b9-97f3-2bdfd723cadf",
                }
                """;
        stompSession.send(SEND_PRIVATE_MESSAGE, messageJSON);

        ErrorMessageDto receivedMsg  = errorMessageDtoCompletableFuture.get(5, TimeUnit.SECONDS);
        Assertions.assertEquals(MessageType.ERROR, receivedMsg.messageType());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), receivedMsg.error().code());
    }

    private List<Transport> createClientTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class StompSessionHandlerAdapterExt extends StompSessionHandlerAdapter { }
}