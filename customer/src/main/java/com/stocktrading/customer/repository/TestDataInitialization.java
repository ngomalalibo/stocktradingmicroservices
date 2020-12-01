package com.stocktrading.customer.repository;

import com.stocktrading.customer.database.MongoConnectionImpl;
import com.stocktrading.customer.entity.ActivityLog;
import com.stocktrading.customer.entity.Client;
import com.stocktrading.customer.entity.ClientAccount;
import com.stocktrading.customer.entity.PersistingBaseEntity;
import com.stocktrading.customer.enumeration.ActivityLogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestDataInitialization
{
    
    private Client client;
    private ClientAccount clientAccount;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    @Autowired
    private static MongoConnectionImpl database;
    
    public static void main(String[] args)
    {
        database.startDB();
        log.info("initializing database");
        new TestDataInitialization();
        log.info("Initialization complete");
        database.stopDB();
        
    }
    
    public TestDataInitialization()
    {

//        initData();
    }
    
    
    public boolean initData()
    {
        /**
         * Load Test Data in correct sequence
         * */
        client = initializeClient();
        clientAccount = initializeClientAccount();
        initializeDB(); // persist test data
        
        return true;
    }
    
    private void initializeDB()
    {
        ActivityLog activityLog = initializeActivityLog();
        persistingBaseEntity.save(activityLog);
        
        Client client = initializeClient();
        persistingBaseEntity.save(client);
    }
    
    public ClientAccount initializeClientAccount()
    {
        return new ClientAccount(90000000D, 6000D, "john.snow@got.com");
    }
    
    public Client initializeClient()
    {
        Client client = new Client("John", "Snow", "john.snow@got.com");
        client.setClientAccountID(clientAccount.getClientID());
        return client;
    }
    
    public ActivityLog initializeActivityLog()
    {
        
        return new ActivityLog("SYSTEM", "Access", "login", ActivityLogType.INFO, "ActivityLog");
    }
}
