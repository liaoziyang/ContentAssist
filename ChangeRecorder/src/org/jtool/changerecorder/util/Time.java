/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.util;

import java.util.Calendar;

/**
 * Manages and formats time information.
 * @author Katsuhisa Maruyama
 */
public class Time {
    
    /**
     * Obtains the current time.
     * @return the current time
     */
    public static long getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }
    
    /**
     * Obtains the formated time information.
     * @param time the time information
     * @return the formatted string of the time
     */
    public static String toUsefulFormat(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        
        return String.format("%1$tY/%1$tm/%1$td %1$tT", cal);
    }
    
    /**
     * Obtains time from the string format.
     * @param str the string representing the time
     * @return the time
     */
    public static long parseTime(String str) {
        str = str.trim();
        str = str.replace('/', ' ');
        str = str.replace(':', ' ');
        String[] strs = str.split(" ");
        if (strs.length < 3 || 6 < strs.length) {
            return -1;
        }
        
        int year, month, day, hour, min, sec;
        try {
            year  = Integer.parseInt(strs[0]);
            month = Integer.parseInt(strs[1]);
            day   = Integer.parseInt(strs[2]);
            hour  = Integer.parseInt(strs[3]);
            min   = Integer.parseInt(strs[4]);
            sec   = Integer.parseInt(strs[5]);
        } catch (Exception e) {
            return -1;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, min, sec);
        return cal.getTimeInMillis();
    }
    
    /**
     * Obtains the year information.
     * @param time the time information
     * @return the year information
     */
    public static int getYear(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * Obtains the month information.
     * @param time the time information
     * @return the month information
     */
    public static int getMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    /**
     * Obtains the day information.
     * @param time the time information
     * @return the day information
     */
    public static int getDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Obtains time information with the YYYYMMDD format.
     * @param time the time information
     * @return the formated string of the time
     */
    public static String YYYYMMDD(long time) {
        return String.format("%04d%02d%02d", getYear(time), getMonth(time), getDay(time));
    }
    
    /**
     * Tests if the time information is valid.
     * @param time the time information
     * @return <code>true</code> if the time information is valid, otherwise <code>false</code>
     */
    public static boolean isValid(long time) {
        return 0 <= time && time <= Long.MAX_VALUE;
    }
}
