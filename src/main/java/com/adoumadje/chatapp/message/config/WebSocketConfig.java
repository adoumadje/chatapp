package com.adoumadje.chatapp.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${client.url}")
    private String clientUrl;
    @Value("${broker.prefix.handshake}")
    private String handshakePrefix;
    @Value("${broker.prefix.application-message}")
    private String applicationMessagePrefix;
    @Value("${broker.prefix.destination}")
    private String destinationPrefix;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(handshakePrefix).setAllowedOriginPatterns(clientUrl).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry broker) {
        broker.enableSimpleBroker(destinationPrefix);
        broker.setApplicationDestinationPrefixes(applicationMessagePrefix);
    }
}
