package com.stocktrading.zuulsvr.database;

import com.mongodb.client.MongoCollection;
import com.stocktrading.zuulsvr.entity.PersistingBaseEntity;
import com.stocktrading.zuulsvr.enumeration.IDPrefixes;

import java.util.HashMap;

public interface CollectionEntityMapping
{
    HashMap<String, ?> mapCollectionsAndEntities();
    
    HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames();
    
    HashMap<String, Class<?>> mapObjectAndClazzNames();
    
    HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes();
    
    public abstract Object getCollectionFromEntityName(String name);
    
}
