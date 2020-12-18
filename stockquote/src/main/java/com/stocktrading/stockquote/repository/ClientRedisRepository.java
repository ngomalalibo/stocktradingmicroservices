package com.stocktrading.stockquote.repository;

import com.stocktrading.stockquote.entity.Client;

public interface ClientRedisRepository
{
    void saveClient(Client client);
    
    void updateClient(Client client);
    
    void deleteClient(String clientId);
    
    Client findClient(String clientId);
}