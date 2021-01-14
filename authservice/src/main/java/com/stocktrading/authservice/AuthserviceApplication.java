package com.stocktrading.authservice;

import com.stocktrading.authservice.database.MongoConnectionImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableEurekaClient
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
