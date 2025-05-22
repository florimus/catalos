package com.commerce.catalos.core.utils;

import java.util.List;

public class StringUtils {

    /**
     * Populates a list of string from a given string that is separated by a
     * comma.
     * 
     * @param input the string to be populated
     * @return the populated list
     */
    public static List<String> populateArrayFromString(String input) {
        return List.of(input.split(","));
    }
}
