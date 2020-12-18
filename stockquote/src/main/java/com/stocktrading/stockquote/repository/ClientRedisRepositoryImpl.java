package com.stocktrading.stockquote.repository;

import com.stocktrading.stockquote.entity.Client;
import com.stocktrading.stockquote.entity.PersistingBaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class ClientRedisRepositoryImpl implements ClientRedisRepository
{
    private static final String HASH_NAME = "client";
    
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, Client> redisTemplate;
    private HashOperations<String, String, PersistingBaseEntity> hashOperations;
    
    public ClientRedisRepositoryImpl()
    {
        super();
    }
    
    @PostConstruct
    private void init()
    {
        hashOperations = redisTemplate.opsForHash();
    }
    
    @Override
    public void saveClient(Client client)
    {
        hashOperations.put(HASH_NAME, client.getUuid(), client);
    }
    
    @Override
    public void updateClient(Client client)
    {
        hashOperations.put(HASH_NAME, client.getUuid(), client);
    }
    
    @Override
    public void deleteClient(String clientId)
    {
        hashOperations.delete(HASH_NAME, clientId);
    }
    
    @Override
    public Client findClient(String clientId)
    {
        return (Client) hashOperations.get(HASH_NAME, clientId);
    }
}