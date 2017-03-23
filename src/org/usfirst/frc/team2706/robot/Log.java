package org.usfirst.frc.team2706.robot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class Log {

    public static final String LOGGER_TABLE = "logging-level";
    
    public static final String ROOT_LOGGER_NAME = "";

    private static final Logger logger = Logger.getLogger(ROOT_LOGGER_NAME);
    
    private static ITableListener updateListener;

    public static String getCallerClassName() throws ClassNotFoundException {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        return Class.forName(stElements[4].getClassName()).getSimpleName() + "."
                        + stElements[4].getMethodName();
    }

    /**
     * Configures the logger
     */
    public static void setUpLogging() {
        ConsoleHandler ch = new ConsoleHandler();
        
        try {
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }

            ch.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    Date dt = new Date(record.getMillis());
                    String S = sdf.format(dt);

                    return record.getLevel() + " " + S + " " + record.getSourceClassName() + "."
                                    + record.getSourceMethodName() + "() " + record.getLoggerName()
                                    + " " + record.getMessage() + "\n";
                }
            });

            logger.addHandler(ch);

            ch.setLevel(Level.ALL);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    ch.flush();
                    ch.close();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        
        updateListener = new ITableListener() {
            @Override
            public void valueChanged(ITable source, String key, Object value, boolean isNew) {
                    Level level = Level.parse(((int)source.getNumber(key, Level.ALL.intValue()))+"");
                    ch.setLevel(level);
                    logger.setLevel(level);
            }
        };
        
        NetworkTable.getTable(LOGGER_TABLE).addTableListener(updateListener);
    }

    public static void d(Object name, Object message) {
        LogLevels.DEBUG.log(name, message);
    }

    public static void d(Object name, Object message, Throwable t) {
        LogLevels.DEBUG.log(name, message, t);
    }

    public static void i(Object name, Object message) {
        LogLevels.INFO.log(name, message);
    }

    public static void i(Object name, Object message, Throwable t) {
        LogLevels.INFO.log(name, message, t);
    }

    public static void w(Object name, Object message) {
        LogLevels.WARNING.log(name, message);
    }

    public static void w(Object name, Object message, Throwable t) {
        LogLevels.WARNING.log(name, message, t);
    }

    public static void e(Object name, Object message) {
        LogLevels.ERROR.log(name, message);
    }

    public static void e(Object name, Object message, Throwable t) {
        LogLevels.ERROR.log(name, message, t);
    }

    private static enum LogLevels {
        DEBUG(Level.CONFIG), INFO(Level.INFO), WARNING(Level.WARNING), ERROR(Level.SEVERE);

        private final Level level;

        private LogLevels(Level level) {
            this.level = level;
        }

        public void log(Object name, Object message) {
            String[] cm = new String[] {"Unknown", "Unknown"};

            try {
                cm = getCallerClassName().split("\\.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Logger.getLogger(name.toString()).logp(level, cm[0], cm[1], message.toString());
        }

        public void log(Object name, Object message, Throwable t) {
            String[] cm = new String[] {"Unknown", "Unknown"};

            try {
                cm = getCallerClassName().split("\\.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Logger.getLogger(name.toString()).logp(level, cm[0], cm[1], message.toString());
        }
    }
}
