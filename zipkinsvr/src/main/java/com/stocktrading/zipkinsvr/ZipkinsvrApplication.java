package com.stocktrading.zipkinsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ZipkinsvrApplication
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(ZipkinsvrApplication.class, args);
    }
}
