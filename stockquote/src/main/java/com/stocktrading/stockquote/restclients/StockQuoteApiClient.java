package com.stocktrading.stockquote.restclients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stocktrading.stockquote.entity.StockQuote;
import com.stocktrading.stockquote.exception.CustomNullPointerException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

@Data
@Slf4j
@Service
@DefaultProperties(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")})
public class StockQuoteApiClient
{
    private static Client client;
    private static WebTarget target;
    
    public static String SECRET_STOCK_API_KEY = System.getenv().get("SECRET_STOCK_API_KEY");
    public static String PUBLISHABLE_STOCK_API_KEY = System.getenv().get("PUBLISHABLE_STOCK_API_KEY");
    public static String URL_ADD_COMPANY_TOKEN = "https://cloud-sse.iexapis.com/stable/stock/%s/quote?token=%s";
    public static String STOCK_URL = "";
    
    protected void setCompanyURL(String company)
    {
        client = ClientBuilder.newClient();
        STOCK_URL = String.format(URL_ADD_COMPANY_TOKEN.trim(), company, PUBLISHABLE_STOCK_API_KEY);
        target = client.target(STOCK_URL);
    }
    
    private StockQuote buildFallbackStockList(String company)
    {
        return new StockQuote("nflx", 50D);
    }
    
    @HystrixCommand(
            commandProperties =
                    {
                            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000"),
                            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5"),
                            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
                    },
            fallbackMethod = "buildFallbackStockList",
            threadPoolKey = "stockThreadPool", threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "30"), @HystrixProperty(name = "maxQueueSize", value = "10")})
    public StockQuote getStock(String company) throws CustomNullPointerException
    {
        //log.info("*****************Hystrix enabled method executing***********");
        //randomlyRunLong();
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        // provide page number to url
        setCompanyURL(company);
        
        URL url;
        
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            
            // send GET request to retrieve data from API as a request for JSON
            // log.info("STOCK URL -> " + STOCK_URL);
            url = new URL(STOCK_URL);
            URLConnection yc = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("ACCEPT", "application/json");
            
            // log.warn("Response code -> " + conn.getResponseCode());
            // log.warn("Response message -> " + conn.getResponseMessage());
            
            // confirm that response is successful with status code 200
            if (conn.getResponseCode() == 200)
            {
                // Create buffered reader to read data
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                
                if ((output = in.readLine()) != null)
                {
                    /*JsonElement je = jp.parse(output);
                    String prettyJsonResponse = gson.toJson(je);
                    log.info("output -> \n" + prettyJsonResponse);*/
                    
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
                    
                    return objectMapper.readValue(output, StockQuote.class);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void randomlyRunLong()
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3)
        {
            sleep();
        }
    }
    
    private void sleep()
    {
        try
        {
            Thread.sleep(11000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    
}
