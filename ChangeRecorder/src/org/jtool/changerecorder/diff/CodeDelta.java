/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.diff;

/**
 * Manages the delta of code (the difference between two source files).
 * @author Katsuhisa Maruyama
 */
class CodeDelta {
    
    /**
     * The type of code change operation.
     */
    enum Type {
      DELETE, INSERT, NULL
    }
    
    /**
     * The offset value of the text that contains this code delta.
     */
    private int offset;
    
    /**
     * The type of this code delta.
     */
    private Type type;
    
    /**
     * The text of the code delta.
     */
    private String text;
    
    /**
     * Creates an instance that stores information on change.
     * @param offset the offset value of the location where the change was applied
     * @param type the type of the change
     * @param text
     */
    CodeDelta(int offset, Type type, String text) {
        this.offset = offset;
        this.type = type;
        this.text = text;
    }
    
    /**
     * Returns the offset value of the text that indicates the start point of the change.
     * @return the offset value for the change
     */
    int getOffset() {
        return offset;
    }
    
    /**
     * Returns the type of the change.
     * @return the code change type
     */
    Type getType() {
        return type;
    }
    
    /**
     * Returns the text that was inserted or deleted by the change.
     * @return the changed text
     */
    String getText() {
        return text;
    }
    
    /**
     * Returns the string for printing
     * @return the string information
     */
    public String toString() {
        return "-- Delta [" + String.valueOf(offset) + "] "+ type + " (" + text + ")";
    }
}
