package org.emeraldcraft.finalproject.pof;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Logger {
	public static void debug(String msg) {
        System.out.println("[DEBUG " + getCurrentTime() + "]: " + msg);
	}
	private static String getCurrentTime(){
        Date date = new Date();
        DateFormat formatter;
        formatter = new SimpleDateFormat("h:mm:ss a");

        formatter.setTimeZone(TimeZone.getDefault());
        String currentTime;
        currentTime = formatter.format(date);
        return currentTime;
    }
}
