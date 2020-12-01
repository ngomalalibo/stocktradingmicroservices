package com.stocktrading.zuulsvr.config;


import com.stocktrading.zuulsvr.entity.User;
import com.stocktrading.zuulsvr.repository.GenericDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class DataConfig
{
    @Bean
    public GenericDataRepository userDataRepository()
    {
        return new GenericDataRepository(new User());
    }
}
