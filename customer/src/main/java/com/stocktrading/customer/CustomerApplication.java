package com.stocktrading.customer;

import com.stocktrading.customer.database.MongoConnectionImpl;
import com.stocktrading.customer.util.UserContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.Filter;

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
