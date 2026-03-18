package com.adoumadje.chatapp.message.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private UUID senderId;
    private UUID receiverId;

    private String CONNECT_URL;
    private String SEND_PRIVATE_MESSAGE;
    private String SUBSCRIBE_PRIVATE_MESSAGE;

    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        receiverId = UUID.randomUUID();

        CONNECT_URL = "http://locahost:" + localServerPort + handshakePrefix;
        SEND_PRIVATE_MESSAGE = applicationMessagePrefix + "/private-message";
        SUBSCRIBE_PRIVATE_MESSAGE = privateMessagePrefix + "/" + receiverId;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testHandlePrivateMessage() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createClientTransport()));

    }

    private List<Transport> createClientTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
}