package com.commerce.catalos.core.utils;

import java.util.Date;

public class TimeUtils {

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
}
