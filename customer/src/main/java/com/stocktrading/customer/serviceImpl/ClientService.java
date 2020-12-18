package com.stocktrading.customer.serviceImpl;

import brave.Span;
import brave.Tracer;
import com.stocktrading.customer.database.MongoConnectionImpl;
import com.stocktrading.customer.entity.Client;
import com.stocktrading.customer.event.SimpleSourceBean;
import com.stocktrading.customer.repository.GenericDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService
{
    @Autowired
    private GenericDataRepository clientDataRepository;
    
    @Autowired
    SimpleSourceBean simpleSourceBean;
    
    @Autowired
    Tracer tracer;
    
    public Client getClient(String clientId)
    {
        Span newSpan = tracer.currentSpan().name("clientMongoDBCall");
        try
        {
            simpleSourceBean.publicClientChange("READ", clientId);
            return (Client) clientDataRepository.getRecordByEntityProperty("_id", clientId);
        }
        finally
        {
            newSpan.tag("peer.service", "mongoDB");
            newSpan.finish();
        }
        
    }
    
    public Client saveClient(Client client)
    {
        //client.setUuid(UUID.randomUUID().toString());
        
        client.save(client);
        simpleSourceBean.publicClientChange("SAVE", client.getUuid());
        return client;
    }
    
    public void updateClient(Client client)
    {
        client.save(client);
        simpleSourceBean.publicClientChange("UPDATE", client.getUuid());
        
    }
    
    public Client deleteClient(Client client)
    {
        client.delete(MongoConnectionImpl.DB_CLIENT, client);
        simpleSourceBean.publicClientChange("DELETE", client.getUuid());
        
        return client;
    }
}