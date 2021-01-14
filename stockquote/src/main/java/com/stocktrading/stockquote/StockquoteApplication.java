package com.stocktrading.stockquote;

import com.netflix.appinfo.AmazonInfo;
import com.stocktrading.stockquote.config.ServiceConfig;
import com.stocktrading.stockquote.database.MongoConnectionImpl;
import com.stocktrading.stockquote.entity.Client;
import com.stocktrading.stockquote.entity.ClientChangeModel;
import com.stocktrading.stockquote.repository.ClientRedisRepository;
import com.stocktrading.stockquote.util.UserContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
@EnableEurekaClient
@Slf4j
@EnableBinding(Sink.class)
public class StockquoteApplication extends SpringBootServletInitializer
{
    @Autowired
    private ServiceConfig serviceConfig;
    
    @Autowired
    ClientRedisRepository clientRedisRepository;
    
    private MongoConnectionImpl database = new MongoConnectionImpl();
    
    @StreamListener(Sink.INPUT)
    public void loggerSink(ClientChangeModel clientChangeModel)
    {
        log.debug("Received an event for client id {}", clientChangeModel.getClientId());
        // TODO > implement consumption of customer update from message queue
        
        switch (clientChangeModel.getAction())
        {
            case "READ":
                log.info("Received a READ event from the customer service for customer id {}", clientChangeModel.getClientId());
                break;
            case "SAVE":
                log.info("Received a SAVE event from the customer service for customer id {}", clientChangeModel.getClientId());
                break;
            case "UPDATE":
                log.info("Received a UPDATE event from the customer service for customer id {}", clientChangeModel.getClientId());
                clientRedisRepository.deleteClient(clientChangeModel.getClientId());
                break;
            case "DELETE":
                log.info("Received a DELETE event from the customer service for customer id {}", clientChangeModel.getClientId());
                clientRedisRepository.deleteClient(clientChangeModel.getClientId());
                break;
            default:
                log.error("Received an UNKNOWN event from the customer service of type {}", clientChangeModel.getType());
                break;
            
        }
    }
    
    
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
    
    @Bean
    public RedisTemplate<String, Client> redisTemplate()
    {
        RedisTemplate<String, Client> template = new RedisTemplate<String, Client>();
        JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
        jedisConnFactory.setHostName(serviceConfig.getRedisServer());
        jedisConnFactory.setPort(serviceConfig.getRedisPort());
        template.setConnectionFactory(jedisConnFactory);
        return template;
    }
    
    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils)
    {
        EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        b.setDataCenterInfo(info);
        b.setHostname(info.get(AmazonInfo.MetaDataKey.localHostname));
        b.setIpAddress(info.get(AmazonInfo.MetaDataKey.localIpv4));
        b.setVirtualHostName("stockquote");
        b.setSecureVirtualHostName("stockquote");
        b.setAppname("stockquote");
        b.setNonSecurePort(8085);
        b.setInstanceId("18.195.70.159:stockquote:8085");
        b.setPreferIpAddress(true);
        return b;
    }
    
}
