/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

/**
 * Stores an execution macro.
 * @author Katsuhisa Maruyama
 */
public class ExecutionMacro extends Macro {
    
    /**
     * The string representing the contents of this macro.
     */
    private String commandId;
    
    /**
     * Creates an object storing information on an execution macro.
     * @param time the time when the macro started
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param commandId the string representing the contents of the macro
     */
    public ExecutionMacro(long time, String type, String path, String commandId) {
        super(time, time, type, path);
        this.commandId = commandId;
    }
    
    /**
     * Returns the string representing the contents of this macro.
     * @return the information on this macro
     */
    public String getCommandId() {
        return commandId;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "EXEC = " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getCommandId();
    }
}
