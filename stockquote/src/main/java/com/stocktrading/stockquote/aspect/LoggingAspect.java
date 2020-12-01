package com.stocktrading.stockquote.aspect;

import com.mongodb.client.MongoCollection;
import com.stocktrading.stockquote.database.MongoConnectionImpl;
import com.stocktrading.stockquote.entity.ActivityLog;
import com.stocktrading.stockquote.entity.PersistingBaseEntity;
import com.stocktrading.stockquote.enumeration.ActivityLogType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect
{
    
    private MongoConnectionImpl database;
    
    @Autowired
    public LoggingAspect()
    {
        this.database = new MongoConnectionImpl();
    }
    
    MongoCollection logCollection;
    
    @AfterReturning(value = "@annotation(Loggable)", returning = "returnValue")
    public void logMethodCall(JoinPoint joinPoint, Object returnValue)
    {
        activityLogger(joinPoint, ActivityLogType.INFO);
        log.info("Logged via aspect after returning");
    }
    
    @AfterThrowing(value = "@annotation(Loggable)", throwing = "exception")
    public void logMethodCallException(JoinPoint joinPoint, Throwable exception)
    {
        activityLogger(joinPoint, ActivityLogType.ERROR);
        log.info("Cause: {}, Message: {}", exception.getCause(), exception.getMessage());
        log.info("Logged via aspect after throwing");
    }
    
    private void activityLogger(JoinPoint joinPoint, ActivityLogType logType)
    {
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        ActivityLog activityLog;
        if (args != null && args.length > 0)
        {
            PersistingBaseEntity pbe = (PersistingBaseEntity) args[0];
            activityLog = new ActivityLog("User", "Saved(" + pbe.getClass().getSimpleName() + " with " + pbe.getUuid() + ") ",
                                          "Saved(" + pbe.getClass().getSimpleName() + " with " + pbe.getUuid() + ") ", logType, pbe.getClass().getSimpleName());
        }
        else
        {
            activityLog = new ActivityLog("User", "method name -> " + method,
                                          joinPoint.getThis().toString(), logType, joinPoint.getThis().toString());
        }
        logCollection = database.getPersistingCollectionFromClass(activityLog);
        logCollection.insertOne(activityLog);
    }
}
