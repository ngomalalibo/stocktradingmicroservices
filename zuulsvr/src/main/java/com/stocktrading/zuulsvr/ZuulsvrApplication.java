package com.stocktrading.zuulsvr;

import com.stocktrading.zuulsvr.database.MongoConnectionImpl;
import com.stocktrading.zuulsvr.util.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulsvrApplication
{
    MongoConnectionImpl database = new MongoConnectionImpl();
    
    public static void main(String[] args)
    {
        SpringApplication.run(ZuulsvrApplication.class, args);
    }
    
    @LoadBalanced
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
