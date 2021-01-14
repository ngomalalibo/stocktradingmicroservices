package com.stocktrading.customer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig
{
    @Bean
    public NewTopic topicExample()
    {
        return TopicBuilder.name("clientChangeTopic")
                           .partitions(3)
                           .replicas(1)
                           .compact()
                           .build();
    }
}
