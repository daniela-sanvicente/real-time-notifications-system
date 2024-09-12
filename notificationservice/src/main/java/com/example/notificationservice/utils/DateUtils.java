package com.example.notificationservice.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    
    public static Instant now() {
        return Instant.now();
    }

    public static String formatInstant(Instant instant){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
