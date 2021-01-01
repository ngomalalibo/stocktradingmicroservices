package com.stocktrading.authservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig
{
    @Value("${signing.key}")
    private String jwtSigningKey = "345345fsdgsf5345";
    
    
    public String getJwtSigningKey()
    {
        return jwtSigningKey;
    }
    
}