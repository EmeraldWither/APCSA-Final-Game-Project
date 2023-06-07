/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Features a more advanced logging functionality
 */
public class Logger
{
    public static void log(String msg)
    {
        //get the class that called our method
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[2];
        String className = element.getClassName();
        //remove package and add .java at the end
        String name = className.split("\\.")[className.split("\\.").length - 1] + ".java";

        System.out.println("(" + name + ":" + element.getLineNumber() + ") [" + getCurrentTime() + "]: " + msg);
    }

    public static void warn(String msg)
    {
        //get the class that called our method
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[2];
        String className = element.getClassName();
        //remove package
        String name = className.split("\\.")[className.split("\\.").length - 1] + ".java";

        System.out.println("<!> (" + name + ":" + element.getLineNumber() + ") [WARNING " + getCurrentTime() + "]: " + msg + "<!>");
    }

    private static String getCurrentTime()
    {
        Date date = new Date();
        DateFormat formatter;
        formatter = new SimpleDateFormat("h:mm:ss a");

        formatter.setTimeZone(TimeZone.getDefault());
        String currentTime;
        currentTime = formatter.format(date);
        return currentTime;
    }

    /**
     * The message formmated in a way that shows that it had came from a command
     *
     * @param command The command name
     * @param msg The message to return to the user
     */
    public static void command(String command, String msg)
    {
        System.out.println("    > (COMMAND \"" + command + "\") [" + getCurrentTime() + "]: " + msg);
    }
}
