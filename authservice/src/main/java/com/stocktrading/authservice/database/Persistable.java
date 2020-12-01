package com.stocktrading.authservice.database;

import com.stocktrading.authservice.entity.PersistingBaseEntity;

import java.util.List;

public interface Persistable
{
    
    <T extends PersistingBaseEntity> void prePersist(T t);
    
    <T extends PersistingBaseEntity> PersistingBaseEntity save(T t);
    
    <T extends PersistingBaseEntity> boolean delete(String collectionName, T t);
    
    <T extends PersistingBaseEntity> boolean deleteMany(List<T> t, String collectionName);
    
    <T extends PersistingBaseEntity> T replaceEntity(T oldEntity, T newEntity);
}
