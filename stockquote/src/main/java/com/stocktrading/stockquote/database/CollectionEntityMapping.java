package com.stocktrading.stockquote.database;

import com.mongodb.client.MongoCollection;
import com.stocktrading.stockquote.entity.PersistingBaseEntity;
import com.stocktrading.stockquote.enumeration.IDPrefixes;

import java.util.HashMap;

public interface CollectionEntityMapping
{
    HashMap<String, ?> mapCollectionsAndEntities();
    
    HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames();
    
    HashMap<String, Class<?>> mapObjectAndClazzNames();
    
    HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes();
    
    public MongoCollection getCollectionFromEntityName(String entityName);
}
