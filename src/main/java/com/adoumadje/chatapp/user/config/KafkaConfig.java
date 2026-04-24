package com.adoumadje.chatapp.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
    @Value("$user.events.topic.name")
    private String userEventsTopicName;

    private final static int TOPIC_REPLICATION_FACTOR = 1;
    private final static int TOPIC_PARTITIONS = 1;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    NewTopic userEventsTopic() {
        return TopicBuilder.name(userEventsTopicName)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .partitions(TOPIC_PARTITIONS)
                .build();
    }
}
