package com.stocktrading.customer;

import com.stocktrading.customer.database.MongoConnectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
// @RefreshScope
// @EnableCircuitBreaker
@EnableBinding(Source.class)
@EnableEurekaClient
public class CustomerApplication
{
    // used yml configuration
    
    /*@Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils)
    {
        EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        b.setDataCenterInfo(info);
        b.setHostname(info.get(AmazonInfo.MetaDataKey.localHostname));
        b.setIpAddress(info.get(AmazonInfo.MetaDataKey.localIpv4));
        b.setVirtualHostName("customer");
        b.setSecureVirtualHostName("customer");
        b.setAppname("customer");
        b.setNonSecurePort(8081);
        b.setAppname("customer");
        b.setInstanceId("52.59.228.171:customer:8081");
        b.setPreferIpAddress(true);
        return b;
    }*/
    
    @Autowired
    MongoConnectionImpl database;
    
    public static void main(String[] args)
    {
        SpringApplication.run(CustomerApplication.class, args);
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
    
    /*@Bean
    public Filter userContextFilter()
    {
        UserContextFilter userContextFilter = new UserContextFilter();
        return userContextFilter;
    }*/
    
}
