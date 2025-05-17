package com.commerce.catalos.core.configurations;

import org.slf4j.LoggerFactory;

public class Logger {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    public static void info(String uuid, String message, Object... args) {
        log.info("{}: " + message, uuid, args);
    }

    public static void error(String uuid, String message, Object... args) {
        log.error("{}: " + message, uuid, args);
    }

    public static void warn(String uuid, String message, Object... args) {
        log.warn("{}: " + message, uuid, args);
    }
}
