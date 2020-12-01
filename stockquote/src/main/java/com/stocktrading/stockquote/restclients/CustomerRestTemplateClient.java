package com.stocktrading.stockquote.restclients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stocktrading.stockquote.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerRestTemplateClient
{
    @Qualifier("getRestTemplate")
    @Autowired
    RestTemplate restTemplate;
    
    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")})
    @LoadBalanced
    public Client getCustomer(String id)
    {
        ResponseEntity<Client> restExchange =
                restTemplate.exchange(
                        "http://localhost:5555/cust/client/{id}",
                        HttpMethod.GET,
                        null, Client.class, id);
        return restExchange.getBody();
    }
    
    
}
