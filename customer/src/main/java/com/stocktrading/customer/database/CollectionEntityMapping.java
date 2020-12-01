package com.stocktrading.customer.database;

import com.mongodb.client.MongoCollection;
import com.stocktrading.customer.entity.PersistingBaseEntity;
import com.stocktrading.customer.enumeration.IDPrefixes;

import java.util.HashMap;

public interface CollectionEntityMapping
{
    HashMap<String, ?> mapCollectionsAndEntities();
    
    HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames();
    
    HashMap<String, Class<?>> mapObjectAndClazzNames();
    
    HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes();
    
    public abstract Object getCollectionFromEntityName(String name);
}
