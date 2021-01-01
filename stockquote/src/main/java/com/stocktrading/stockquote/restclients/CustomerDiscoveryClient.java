package com.stocktrading.stockquote.restclients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stocktrading.stockquote.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CustomerDiscoveryClient
{
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Qualifier("getRestTemplate")
    @Autowired
    RestTemplate restTemplate;
    
    @LoadBalanced
    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")})
    public Client getCustomer(String id)
    {
        // TODO > Check if request header has token and validate it against authserver
        List<ServiceInstance> instances =
                discoveryClient.getInstances("authservice");
        if (instances.size() == 0)
        {
            return null;
        }
        String serviceUri = String.format("http://zuulserver:5555/authservice/currentuser");
        ResponseEntity<Client> restExchange = restTemplate.exchange(serviceUri, HttpMethod.GET, null, Client.class, id);
        
        
        //RestTemplate restTemplate = new RestTemplate();
        instances =
                discoveryClient.getInstances("customer");
        if (instances.size() == 0)
        {
            return null;
        }
        serviceUri = String.format("%s/client/%s", instances.get(0).getUri().toString(), id);
        System.out.println("serviceUri -> " + serviceUri);
        restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Client.class, id);
        return restExchange.getBody();
    }
    
    
}
