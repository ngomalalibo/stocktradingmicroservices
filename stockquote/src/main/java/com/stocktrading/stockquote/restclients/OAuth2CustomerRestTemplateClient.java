package com.stocktrading.stockquote.restclients;

import com.stocktrading.stockquote.entity.Client;
import com.stocktrading.stockquote.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2CustomerRestTemplateClient
{
    @Qualifier("oauth2RestTemplate")
    @Autowired
    OAuth2RestTemplate restTemplate;
    
    @Value("${zuul.uri}")
    private String ZUUL_URI;
    
    @Autowired
    private UserContext userContext;
    
    public Client getCustomer(String id)
    {
        log.debug("In Slock client: {}", userContext.getCorrelationId());
        
        ResponseEntity<Client> restExchange =
                restTemplate.exchange(
                        ZUUL_URI+"/cust/client/{id}",
                        HttpMethod.GET,
                        null, Client.class, id);
        
        return restExchange.getBody();
    }
}