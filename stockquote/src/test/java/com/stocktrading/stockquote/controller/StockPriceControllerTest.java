package com.stocktrading.stockquote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class StockPriceControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup()
    {
    }
    
    @Test
    void getStockPrice() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/stockprice/{companyname}", "nflx")
                                              .contentType("application/json")/*
                                              .param("token", RegistrationService.token)*/)
               .andExpect(MockMvcResultMatchers.status().isOk());
        
        
    }
}