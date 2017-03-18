package org.usfirst.frc.team2706.robot;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class Log {

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

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, false);
        tr.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }
    
    private static enum LogLevels {
        DEBUG(Level.CONFIG), INFO(Level.INFO), WARNING(Level.WARNING), ERROR(Level.SEVERE);

        private final Level level;

        private LogLevels(Level level) {
            this.level = level;
        }

        public void log(Object name, Object message) {
            System.out.println(level.getName() + " " + name + " " + message);
        }

        public void log(Object name, Object message, Throwable t) {
            System.out.println(level.getName() + " " + name +  " "  + getStackTraceString(t));
        }
    }
}
