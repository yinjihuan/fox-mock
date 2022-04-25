package com.cxytiandi.foxmock.agent.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public abstract class Logger {


    public static Logger getLogger(Class<?> clazz) {
        return new StdOutLogger(clazz);
    }

    final Class<?> logClass;

    private Logger(Class<?> logClass) {
        this.logClass = logClass;
    }

    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    public void warn(String msg) {
        log(Level.WARNING, msg, null);
    }

    public void warn(String msg, Throwable thrown) {
        log(Level.WARNING, msg, thrown);
    }

    public void error(String msg) {
        log(Level.SEVERE, msg, null);
    }

    public void error(String msg, Throwable thrown) {
        log(Level.SEVERE, msg, thrown);
    }

    protected abstract void log(Level level, String msg, Throwable thrown);

    private static class StdOutLogger extends Logger {
        StdOutLogger(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public void log(Level level, String msg, Throwable thrown) {
            final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            System.out.printf("%s %s [%s] %s: %s%n", time, level, Thread.currentThread().getName(), logClass.getSimpleName(), msg);
            if (thrown != null) {
                thrown.printStackTrace(System.out);
            }
        }
    }
}
