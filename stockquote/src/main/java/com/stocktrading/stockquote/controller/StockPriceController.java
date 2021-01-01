package com.stocktrading.stockquote.controller;

import com.stocktrading.stockquote.entity.Client;
import com.stocktrading.stockquote.entity.StockQuote;
import com.stocktrading.stockquote.restclients.CustomerDiscoveryClient;
import com.stocktrading.stockquote.restclients.CustomerFeignClient;
import com.stocktrading.stockquote.restclients.CustomerRestTemplateClient;
import com.stocktrading.stockquote.restclients.OAuth2CustomerRestTemplateClient;
import com.stocktrading.stockquote.serviceImpl.TransactionService;
import com.stocktrading.stockquote.util.UserContextFilter;
import com.stocktrading.stockquote.util.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class StockPriceController
{
    Logger logger = LoggerFactory.getLogger(UserContextFilter.class);
    
    @Autowired
    @Qualifier("stockQuoteService")
    TransactionService services;
    
    @Autowired
    CustomerDiscoveryClient customerDiscoveryClient;
    @Autowired
    CustomerFeignClient customerFeignClient;
    @Autowired
    CustomerRestTemplateClient customerRestTemplateClient;
    @Autowired
    OAuth2CustomerRestTemplateClient oAuth2CustomerRestTemplateClient;
    
    
    
    String id = "CLI-cd988e7e-b575-4a01-b970-5742e8fec9cc";
    
    @GetMapping("/stockprice/{companyname}")
    public ResponseEntity<Object> checkStockPrice(@PathVariable(name = "companyname") String companyname)
    {
        logger.info("StockQuote CorrelationID: {}", UserContextHolder.getContext().getCorrelationId());
        Map<String, Object> request = new HashMap<>();
        request.put("companyname", companyname);
        StockQuote stockQuote = (StockQuote) services.service(request);
        
        if (stockQuote != null)
        {
            Client restTemplateCustomer = customerRestTemplateClient.getCustomer(id);
            System.out.println("restTemplateCustomer = " + restTemplateCustomer.getFirstName());
            Client restOauthTemplateCustomer = oAuth2CustomerRestTemplateClient.getCustomer(id);
            System.out.println("oAuth2CustomerRestTemplateClient = " + restOauthTemplateCustomer.getFirstName());
            
            /*Client discoveryCustomer = customerDiscoveryClient.getCustomer(id); // Discovery option
            Client feignCustomer = customerFeignClient.getCustomer(id); // Feign Client option
            Client restTemplateCustomer = customerRestTemplateClient.getCustomer(id); // RestTemplate option
            
            System.out.println("discoveryCustomer = " + discoveryCustomer.getFirstName());
            System.out.println("restTemplateCustomer = " + restTemplateCustomer.getFirstName());
            System.out.println("feignCustomer = " + feignCustomer.getFirstName());*/
            
            return ok(stockQuote);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
}
