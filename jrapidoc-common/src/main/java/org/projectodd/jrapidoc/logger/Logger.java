package org.projectodd.jrapidoc.logger;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class Logger {

    private static Log log = new SystemStreamLog();

    public static void setLogger(org.apache.maven.plugin.logging.Log logger) {
        log = logger;
    }

    public static void error(Throwable t, String msg, String... tokens) {
        log.error(format(msg, tokens), t);
    }

    public static void error(String msg, String... tokens) {
        log.error(format(msg, tokens));
    }

    public static void warn(Throwable t, String msg, String... tokens) {
        log.warn(format(msg, tokens), t);
    }

    public static void warn(String msg, String... tokens) {
        log.warn(format(msg, tokens));
    }

    public static void info(String msg, String... tokens) {
        log.info(format(msg, tokens));
    }

    public static void debug(String msg, String... tokens) {
        log.debug(format(msg, tokens));
    }

    static String format(String msg, String... tokens) {
        if (msg == null) {
            return null;
        }
        try {
            return MessageFormat.format(msg, tokens);
        } catch (IllegalArgumentException e) {
            return msg + " " + Arrays.toString(tokens);
        }
    }
}
