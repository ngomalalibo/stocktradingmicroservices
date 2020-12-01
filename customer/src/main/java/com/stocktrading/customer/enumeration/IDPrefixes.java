package com.stocktrading.customer.enumeration;

public enum IDPrefixes
{
    ActivityLog, Client, ClientAccount, ClientPortfolio, ClientTransaction, StockQuote, Transaction, User, PersistingBaseEntity;
    
    public static String getDisplayText(IDPrefixes idPrefix)
    {
        switch (idPrefix)
        {
            case ActivityLog:
                return "LOG-";
            case Client:
                return "CLI-";
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
            case Client:
                return IDPrefixes.getDisplayText(IDPrefixes.Client);
            case PersistingBaseEntity:
            default:
                return "";
        }
    }
}
