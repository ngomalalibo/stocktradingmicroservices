package com.stocktrading.zuulsvr.util;

import com.mongodb.client.MongoCollection;
import com.stocktrading.zuulsvr.database.MongoConnectionImpl;
import org.springframework.stereotype.Component;

@Component
public class GetCollectionFromEntityName
{
    private static MongoConnectionImpl database = new MongoConnectionImpl();
    
    
}
