package com.stocktrading.customer.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.stocktrading.customer.entity.ActivityLog;
import com.stocktrading.customer.entity.Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.HashSet;

@Slf4j
@Getter
public abstract class DatabaseConnection
{
    
    protected final String DBNAME = "stocks";
    public static final String DB_ORGANIZATION = "Stock Trading Inc.";
    public static final String DB_ACTIVITYLOG = "activitylogs";
    public static final String DB_CLIENT = "clients";
    
    protected String DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
    
    protected static MongoClient mongo = null;
    protected static MongoDatabase db;
    
    public static MongoCollection<ActivityLog> activityLog;
    public static MongoCollection<Client> client;
    
    public abstract MongoDatabase startDB();
    
    public abstract void stopDB();
    
    public abstract MongoDatabase getDBConnection();
    
    public abstract Document getDBStats();
    
    public abstract int createAllCollections();
    
    public abstract void createCollection(HashSet<String> hash, String collection);
    
    
    
}
