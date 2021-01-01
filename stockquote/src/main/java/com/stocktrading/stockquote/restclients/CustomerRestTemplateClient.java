package com.stocktrading.stockquote.restclients;

import brave.Span;
import brave.Tracer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stocktrading.stockquote.entity.Client;
import com.stocktrading.stockquote.repository.ClientRedisRepository;
import com.stocktrading.stockquote.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CustomerRestTemplateClient
{
    @Qualifier("getRestTemplate")
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ClientRedisRepository clientRedisRepo;
    
    @Autowired
    UserContext userContext;
    
    @Autowired
    Tracer tracer;
    
    @Value("${zuul.uri}")
    private String ZUUL_URI;
    
    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")})
    @LoadBalanced
    public Client getCustomer(String clientId)
    {
        
        log.info("In stock Service.getClient: {}", userContext.getCorrelationId());
        
        Client client = checkRedisCache(clientId);
        
        if (client != null)
        {
            log.info("I have successfully retrieved a client {} from the redis cache: {}", clientId, client);
            return client;
        }
        
        log.info("Unable to locate client from the redis cache: {}.", clientId);
        
        ResponseEntity<Client> restExchange =
                restTemplate.exchange(
                        ZUUL_URI + "/cust/client/{id}",
                        HttpMethod.GET,
                        null, Client.class, clientId);
        
        /*Save the record from cache*/
        client = restExchange.getBody();
        
        if (client != null)
        {
            cacheClientObject(client);
        }
        
        return client;
    }
    
    private Client checkRedisCache(String clientId)
    {
        Span newSpan = tracer.currentSpan().name("readClientDataFromRedis");
        try
        {
            return clientRedisRepo.findClient(clientId);
        }
        catch (Exception ex)
        {
            log.error("Error encountered while trying to retrieve client {} check Redis Cache.  Exception {}", clientId, ex);
            return null;
        }
        finally
        {
            newSpan.tag("peer.service", "redis");
            newSpan.finish();
        }
    }
    
    private void cacheClientObject(Client client)
    {
        try
        {
            clientRedisRepo.saveClient(client);
        }
        catch (Exception ex)
        {
            log.error("Unable to cache client {} in Redis. Exception {}", client.getUuid(), ex);
        }
    }
}
