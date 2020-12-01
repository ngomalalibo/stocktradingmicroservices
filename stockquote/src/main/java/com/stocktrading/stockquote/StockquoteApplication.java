package com.stocktrading.stockquote;

import com.stocktrading.stockquote.database.MongoConnectionImpl;
import com.stocktrading.stockquote.util.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class StockquoteApplication extends SpringBootServletInitializer
{
    private MongoConnectionImpl database = new MongoConnectionImpl();
    
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder)
    {
        return builder.sources(StockquoteApplication.class);
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(StockquoteApplication.class, args);
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
    
    /*@LoadBalanced
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }*/
    
    @Bean
    public RestTemplate getRestTemplate()
    {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors == null)
        {
            template.setInterceptors(
                    Collections.singletonList(
                            new UserContextInterceptor()));
        }
        else
        {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }
    
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(
            @Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext,
            OAuth2ProtectedResourceDetails details)
    {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }
}
