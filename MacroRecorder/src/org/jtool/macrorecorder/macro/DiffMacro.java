/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

/**
 * Stores a macro that represents difference between two versions of source code.
 * @author Katsuhisa Maruyama
 */
public class DiffMacro extends DocumentMacro {
    
    /**
     * Creates an object storing information on a difference macro.
     * @param time the time when the macro started and ended
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param start the leftmost offset of the document changed by this macro
     * @param itext the contents of the document inserted by the macro
     * @param dtext the contents of the document deleted by the macro
     */
    public DiffMacro(long time, String type, String path, int start, String itext, String dtext) {
        super(time, time, type, path, start, itext, dtext);
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "DIFF = " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getStart() + " " + "[" + getInsertedText() + "][" + getDeletedText() + "]";
    }
}
