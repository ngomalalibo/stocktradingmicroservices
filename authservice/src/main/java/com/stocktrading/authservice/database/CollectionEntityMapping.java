package com.stocktrading.authservice.database;

import com.mongodb.client.MongoCollection;
import com.stocktrading.authservice.entity.PersistingBaseEntity;
import com.stocktrading.authservice.enumeration.IDPrefixes;

import java.util.HashMap;

public interface CollectionEntityMapping
{
    HashMap<String, ?> mapCollectionsAndEntities();
    
    HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames();
    
    HashMap<String, Class<?>> mapObjectAndClazzNames();
    
    HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes();
    
    public abstract Object getCollectionFromEntityName(String name);
}
