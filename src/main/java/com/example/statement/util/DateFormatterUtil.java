package com.example.statement.util;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateFormatterUtil {

    public static String getPeriodName(Integer month, Integer year)
    {
        String monthName = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        return Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1) + " " + year + " год";
    }
}
