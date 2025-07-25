package com.commerce.catalos.core.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {

    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("d-M-yyyy");

    public static boolean isFutureDate(final Date date) {
        if (date == null) {
            return false;
        }
        return date.after(new Date());
    }

    public static boolean isPastDate(final Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }

    public static Date parseToStartOfDay(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, INPUT_DATE_FORMAT);
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.of("UTC"));
        return Date.from(zdt.toInstant());
    }

    public static Date parseToEndOfDay(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, INPUT_DATE_FORMAT);
        ZonedDateTime zdt = localDate.atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"));
        return Date.from(zdt.toInstant());
    }
}
