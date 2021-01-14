package com.stocktrading.customer.event;

import com.stocktrading.customer.entity.ClientChangeModel;
import com.stocktrading.customer.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleSourceBean
{
    @Autowired
    Source output;
    
    @Autowired
    UserContext userContext;
    
    
    public void publicClientChange(String action, String clientId)
    {
        log.info("Sending Kafka message {} for Client Id: {}", action, clientId);
        ClientChangeModel change = new ClientChangeModel(
                ClientChangeModel.class.getTypeName(),
                action,
                clientId,
                userContext.getCorrelationId());
        
        // source.output().send(MessageBuilder.withPayload(change).build());
        output.output().send(MessageBuilder.withPayload(change).build());
        // kafkaTemplate.send("clientChangeTopic", change);
        log.info("{} kafka message sent for client Id: {}", action, clientId);
    }
    
   /* @Bean
    public void newTopic()
    {
        new NewTopic("clientChangeTopic", 3, (short) 1);
    }*/
}