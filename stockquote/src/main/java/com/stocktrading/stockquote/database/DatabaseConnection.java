package com.stocktrading.stockquote.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.stocktrading.stockquote.entity.ActivityLog;
import com.stocktrading.stockquote.entity.StockQuote;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.HashSet;

@Slf4j
@Getter
public abstract class DatabaseConnection
{
    
    protected final String DBNAME = "stocks";
    protected static final String DB_ORGANIZATION = "Stock Trading Inc.";
    protected static final String DB_ACTIVITYLOG = "activitylogs";
    protected static final String DB_STOCK = "stocks";
    
    protected String DBSTR = System.getenv().get("MONGODB_DATABASE_STOCKS_ATLAS");
    
    protected static MongoClient mongo = null;
    protected static MongoDatabase db;
    
    public static MongoCollection<ActivityLog> activityLog;
    public static MongoCollection<StockQuote> stockQuote;
    
    public abstract MongoDatabase startDB();
    
    public abstract void stopDB();
    
    public abstract MongoDatabase getDBConnection();
    
    public abstract Document getDBStats();
    
    public abstract int createAllCollections();
    
    public abstract void createCollection(HashSet<String> hash, String collection);
    
}
