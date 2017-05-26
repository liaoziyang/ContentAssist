/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

/**
 * Stores a macro occurring copy.
 * @author Katsuhisa Maruyama
 */
public class CopyMacro extends Macro {
    
    /**
     * The leftmost offset of the document copied by this macro.
     */
    private int start;
    
    /**
     * The contents of the document copied by this macro.
     */
    private String copiedText;
    
    /**
     * Creates an object storing information on a copy macro.
     * @param time the time when the macro started and ended
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param start the leftmost offset of the document changed by this macro
     * @param text the contents of the document copied by the macro
     */
    public CopyMacro(long time, String type, String path, int start, String text) {
        super(time, time, type, path);
        this.start = start;
        this.copiedText = text;
    }
    
    /**
     * Returns the leftmost offset of the document changed by this macro.
     * @return the leftmost offset of the document
     */
    public int getStart() {
        return start;
    }
    
    /**
     * Returns the contents of the document copied by this macro.
     * @return the copied contents of the document
     */
    public String getCopiedText() {
        return copiedText;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "COPY = " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getStart() + " " + "[" + getCopiedText() + "]";
    }
}
