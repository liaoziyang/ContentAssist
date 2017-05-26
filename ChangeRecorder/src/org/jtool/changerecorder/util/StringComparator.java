/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.util;

/**
 * Compares two strings.
 * @author Katsuhisa Maruyama
 */
public class StringComparator {
    
    /**
     * Tests if two texts are the same.
     * @param text1 the first text to be compared to the second text
     * @param text2 the second text to be compare to the first text
     * @return <code>true</code> if both the texts are the same, otherwise <code>false</code>
     */
    public static boolean isSame(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return true;
        }
        
        if (text1 == null || text2 == null) {
            return false;
        }
        
        return text1.compareTo(text2) == 0;
    }
    
    /**
     * Tests if two texts are the same, ignoring case differences.
     * @param text1 the first text to be compared to the second text
     * @param text2 the second text to be compare to the first text
     * @return <code>true</code> if the texts are the same, otherwise <code>false</code>
     */
    public static boolean isSameIgnoreCase(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return true;
        }
        
        if (text1 == null || text2 == null) {
            return false;
        }
        
        return text1.compareToIgnoreCase(text2) == 0;
    }
}
