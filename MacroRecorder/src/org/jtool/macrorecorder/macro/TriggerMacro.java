/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

/**
 * Stores a trigger macro.
 * @author Katsuhisa Maruyama
 */
public class TriggerMacro extends Macro {
    
    /**
     * The kinds of a trigger.
     */
    public enum Kind {
        BEGIN, END, CURSOR_CHANGE;
    }
    
    /**
     * The kind of a trigger of the macro.
     */
    private Kind kind = null;
    
    /**
     * Creates an object storing information on a macro.
     * @param time the time when the macro occurred
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param kind the kind of a trigger of the macro
     */
    public TriggerMacro(long time, String type, String path, Kind kind) {
        super(time, time, type, path);
        this.kind = kind;
    }
    
    /**
     * Returns the kind of a trigger of the macro
     * @return the kind of the macro
     */
    public Kind getKind() {
        return kind;
    }
    
    /**
     * Tests this compound macro indicates the beginning.
     * @return <code>true</code> if this compound macro indicates the beginning, otherwise <code>false</code>
     */
    public boolean isBegin() {
        return kind == Kind.BEGIN;
    }
    
    /**
     * Tests this compound macro indicates the ending.
     * @return <code>true</code> if this compound macro indicates the ending, otherwise <code>false</code>
     */
    public boolean isEnd() {
        return kind == Kind.END;
    }
    
    /**
     * Tests this compound macro indicates as the change of the current position of the cursor.
     * @return <code>true</code> if this compound macro indicates the cursor change, otherwise <code>false</code>
     */
    public boolean isCursorChange() {
        return kind == Kind.CURSOR_CHANGE;
    }
    
    
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "TRIGGER(" + getType() + ") = " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getKind().toString();
    }
}
