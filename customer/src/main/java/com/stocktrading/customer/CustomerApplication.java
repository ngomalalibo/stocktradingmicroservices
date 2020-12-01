package com.stocktrading.customer;

import com.stocktrading.customer.database.MongoConnectionImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
// @RefreshScope
// @EnableCircuitBreaker
public class CustomerApplication
{
    MongoConnectionImpl database = new MongoConnectionImpl();
    
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
    
}
