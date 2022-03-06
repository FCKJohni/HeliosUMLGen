package dev.heliosteam.heliosumlgen;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeliosLogger {

    private static Logger logger;

    private HeliosLogger() {

    }

    private static void setup() {
        logger = LogManager.getLogger("HeliosLogger");

    }

    public static void info(String var1) {
        if (logger == null) setup();
        logger.info(var1);
    }

    public static void info(String var1, Object var2) {
        if (logger == null) setup();
        logger.info(var1, var2);
    }

    public static void info(String var1, Object var2, Object var3) {
        if (logger == null) setup();
        logger.info(var1, var2, var3);
    }

    public static void warn(String var1) {
        if (logger == null) setup();
        logger.warn(var1);
    }

    public static void warn(String var1, Object var2) {
        if (logger == null) setup();
        logger.warn(var1, var2);
    }

    public static void warn(String var1, Object var2, Object var3) {
        if (logger == null) setup();
        logger.warn(var1, var2, var3);
    }

    public static void error(String var1) {
        if (logger == null) setup();
        logger.error(var1);
    }

    public static void error(String var1, Object var2) {
        if (logger == null) setup();
        logger.error(var1, var2);
    }

    public static void error(String var1, Object var2, Object var3) {
        if (logger == null) setup();
        logger.error(var1, var2, var3);
    }

    public static void debug(String var1) {
        if (logger == null) setup();
        logger.debug(var1);
    }

    public static void debug(String var1, Object var2) {
        if (logger == null) setup();
        logger.debug(var1, var2);
    }

    public static void debug(String var1, Object var2, Object var3) {
        if (logger == null) setup();
        logger.debug(var1, var2, var3);
    }

    public static void success(String var1) {
        if (logger == null) setup();
        logger.log(Level.getLevel("SUCCESS"), var1);
    }

}
