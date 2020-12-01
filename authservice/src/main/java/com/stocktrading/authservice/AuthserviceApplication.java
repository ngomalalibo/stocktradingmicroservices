package com.stocktrading.authservice;

import com.stocktrading.authservice.database.MongoConnectionImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class AuthserviceApplication
{
    MongoConnectionImpl database = new MongoConnectionImpl();
    
    public static void main(String[] args)
    {
        SpringApplication.run(AuthserviceApplication.class, args);
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
