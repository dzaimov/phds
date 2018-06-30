package com.phds.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static String DATE_FORMAT = "dd.MM.yyyy";

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}
