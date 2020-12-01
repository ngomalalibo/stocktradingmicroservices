package com.stocktrading.zuulsvr.enumeration;

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
            case ClientAccount:
                return "CLI_A";
            case ClientPortfolio:
                return "CLI_P";
            case ClientTransaction:
                return "CLI_T";
            case StockQuote:
                return "SQ";
            case Transaction:
                return "TR";
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
            case Client:
                return IDPrefixes.getDisplayText(IDPrefixes.Client);
            case ClientTransaction:
                return IDPrefixes.getDisplayText(IDPrefixes.ClientTransaction);
            case ClientPortfolio:
                return getDisplayText(ClientPortfolio);
            case StockQuote:
                return getDisplayText(StockQuote);
            case Transaction:
                return getDisplayText(Transaction);
            case User:
                return getDisplayText(User);
            case PersistingBaseEntity:
            default:
                return "";
        }
    }
}
