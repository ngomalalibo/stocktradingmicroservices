package com.stocktrading.customer.event;

import com.stocktrading.customer.entity.ClientChangeModel;
import com.stocktrading.customer.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleSourceBean
{
    @Qualifier("output")
    @Autowired
    MessageChannel messageChannel;
    
    private Source source;
    
    @Autowired
    UserContext userContext;
    
    
    @Autowired
    public SimpleSourceBean(Source source)
    {
        this.source = source;
    }
    
    public void publicClientChange(String action, String clientId)
    {
        log.info("Sending Kafka message {} for Client Id: {}", action, clientId);
        ClientChangeModel change = new ClientChangeModel(
                ClientChangeModel.class.getTypeName(),
                action,
                clientId,
                userContext.getCorrelationId());
        
        this.source.output().send(MessageBuilder.withPayload(change).build());
        messageChannel.send(MessageBuilder.withPayload(change).build());
    }
}