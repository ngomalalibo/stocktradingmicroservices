package com.stocktrading.stockquote.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig
{
    
    @Value("${example.property}")
    private String exampleProperty = "";
    
    private String redisServer = "localhost";
    
    private String redisPort = "6379";
    
    public String getExampleProperty()
    {
        return exampleProperty;
    }
    
    public String getRedisServer()
    {
        return redisServer;
    }
    
    public Integer getRedisPort()
    {
        return new Integer(redisPort).intValue();
    }
    
}