package com.stocktrading.stockquote.restclients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stocktrading.stockquote.entity.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient("customer")
public interface CustomerFeignClient
{
    @RequestMapping(method = RequestMethod.GET, value = "/client/{id}", consumes = "application/json")
    //@HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")})
    Client getCustomer(@PathVariable("id") String id);
}