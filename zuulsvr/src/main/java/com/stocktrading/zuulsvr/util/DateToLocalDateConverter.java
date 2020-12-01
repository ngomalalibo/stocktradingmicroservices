package com.stocktrading.zuulsvr.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateToLocalDateConverter
{
    public static LocalDate dateToLocalDateConverter(Date date)
    {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }
}
