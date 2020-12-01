package com.stocktrading.customer.config;

import com.stocktrading.customer.entity.Client;
import com.stocktrading.customer.repository.GenericDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig
{
    @Bean
    public GenericDataRepository clientDataRepository()
    {
        return new GenericDataRepository(new Client());
    }
}
