package com.stocktrading.authservice.config;


import com.stocktrading.authservice.entity.User;
import com.stocktrading.authservice.repository.GenericDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig
{
    @Bean
    public GenericDataRepository userDataRepository()
    {
        return new GenericDataRepository(new User());
    }
}
