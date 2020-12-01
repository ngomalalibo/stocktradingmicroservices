package com.stocktrading.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocktrading.customer.entity.Client;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class ClientControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void getClient() throws Exception
    {
        String id = "CLI-cd988e7e-b575-4a01-b970-5742e8fec9cc";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/client/{id}", id)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk());
        
    }
    
    @Test
    void newClient() throws Exception
    {
        Client client = new Client("stephen", "Kali", "stephen.kali@got.com");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/client/new")
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(objectMapper.writeValueAsString(client))
        .header("Authorization", "Bearer D4j_1nBMdMs-FRzS6MhX36gTPE4"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}