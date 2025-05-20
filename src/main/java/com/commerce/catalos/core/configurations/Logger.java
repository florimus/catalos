package com.commerce.catalos.core.configurations;

import org.slf4j.LoggerFactory;

public class Logger {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    /**
     * Logs an informational message with the specified UUID and message format.
     *
     * @param uuid    A unique identifier to include in the log entry.
     * @param message The message template to log, which can contain placeholders
     *                for arguments.
     * @param args    Optional arguments to be replaced in the message template.
     */
    public static void info(String uuid, String message, Object... args) {
        log.info("{}: " + message, uuid, args);
    }

    /**
     * Logs an error message with the specified UUID and message format.
     *
     * @param uuid    A unique identifier to include in the log entry.
     * @param message The message template to log, which can contain placeholders
     *                for arguments.
     * @param args    Optional arguments to be replaced in the message template.
     */
    public static void error(String uuid, String message, Object... args) {
        log.error("{}: " + message, uuid, args);
    }

    /**
     * Logs a warning message with the specified UUID and message format.
     *
     * @param uuid    A unique identifier to include in the log entry.
     * @param message The message template to log, which can contain placeholders
     *                for arguments.
     * @param args    Optional arguments to be replaced in the message template.
     */
    public static void warn(String uuid, String message, Object... args) {
        log.warn("{}: " + message, uuid, args);
    }
}
