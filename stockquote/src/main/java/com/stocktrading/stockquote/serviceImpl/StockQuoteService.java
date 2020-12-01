package com.stocktrading.stockquote.serviceImpl;

import com.stocktrading.stockquote.exception.CustomNullPointerException;
import com.stocktrading.stockquote.restclients.StockQuoteApiClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Slf4j
@Service
public class StockQuoteService implements TransactionService
{
    private StockQuoteApiClient stockQuoteApiClient;
    
    @Autowired
    public StockQuoteService(StockQuoteApiClient stockQuoteApiClient)
    {
        this.stockQuoteApiClient = stockQuoteApiClient;
    }
    
    @Override
    public Object service(Map<String, Object> params)
    {
        String company = params.get("companyname").toString();
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        return stockQuoteApiClient.getStock(company);
    }
}
