package com.stocktrading.customer;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.stocktrading.customer.database.MongoConnectionImpl;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
// @RefreshScope
// @EnableCircuitBreaker
@EnableBinding(Source.class)
public class CustomerApplication
{
    @Autowired
    MongoConnectionImpl database;
    
    public static void main(String[] args)
    {
        SpringApplication.run(CustomerApplication.class, args);
    }
    
    @PostConstruct
    public void startDB()
    {
        database.startDB();
    }
    
    @PreDestroy
    public void stopDB()
    {
        database.stopDB();
    }
    
    /*@Bean
    public Filter userContextFilter()
    {
        UserContextFilter userContextFilter = new UserContextFilter();
        return userContextFilter;
    }*/
    
}
