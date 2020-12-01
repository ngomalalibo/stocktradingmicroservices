package com.stocktrading.authservice.enumeration;

public enum IDPrefixes
{
    ActivityLog, User, PersistingBaseEntity;
    
    public static String getDisplayText(IDPrefixes idPrefix)
    {
        switch (idPrefix)
        {
            case ActivityLog:
                return "LOG-";
            case User:
                return "USR";
            case PersistingBaseEntity:
            default:
                return "";
        }
    }
    
    public static <T> String getIdPrefix(T t)
    {
        
        String simpleName = t.getClass().getSimpleName();
        //System.out.println("Simple Class Name: " + t.getClass().getSimpleName());
        
        IDPrefixes idPrefixes = IDPrefixes.valueOf(simpleName);
        switch (idPrefixes)
        {
            case ActivityLog:
                return IDPrefixes.getDisplayText(IDPrefixes.ActivityLog);
            case User:
                return IDPrefixes.getDisplayText(IDPrefixes.User);
            case PersistingBaseEntity:
            default:
                return "";
        }
    }
}
