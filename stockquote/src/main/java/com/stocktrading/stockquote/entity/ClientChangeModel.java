package com.stocktrading.stockquote.entity;

import java.io.Serializable;

public class ClientChangeModel
{
    private String type;
    private String action;
    private String clientId;
    private String correlationId;
    
    
    public ClientChangeModel(String type, String action, String clientId, String correlationId)
    {
        super();
        this.type = type;
        this.action = action;
        this.clientId = clientId;
        this.correlationId = correlationId;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getAction()
    {
        return action;
    }
    
    public void setAction(String action)
    {
        this.action = action;
    }
    
    
    public String getClientId()
    {
        return clientId;
    }
    
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }
    
    
    public String getCorrelationId()
    {
        return correlationId;
    }
    
    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }
    
    
}