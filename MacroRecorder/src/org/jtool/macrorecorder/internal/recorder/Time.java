/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import java.util.Calendar;

/**
 * Obtains time information.
 * @author Katsuhisa Maruyama
 */
class Time {
    
    /**
     * Obtains the current time.
     * @return the current time
     */
    static long getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }
}
