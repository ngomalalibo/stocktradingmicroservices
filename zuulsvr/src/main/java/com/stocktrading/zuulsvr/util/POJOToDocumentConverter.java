package com.stocktrading.zuulsvr.util;

import com.google.gson.Gson;
import com.stocktrading.zuulsvr.entity.PersistingBaseEntity;
import org.bson.Document;

public class POJOToDocumentConverter
{
    public static <T extends PersistingBaseEntity> Document pojoToDocumentConverter(T t)
    {
        // convert pojo to json using Gson and parse using Document.parse()
        
        Gson gson = new Gson();
        Document parse = Document.parse(gson.toJson(t));
        System.out.println("parse = " + parse);
        return parse;
    }
}
