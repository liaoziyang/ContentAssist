/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

import java.util.List;

/**
 * Stores a macro.
 * @author Katsuhisa Maruyama
 */
public class Macro {
    
    /**
     * The time when this macro started.
     */
    protected long startTime;
    
    /**
     * The time when this macro ended.
     */
    protected long endTime;
    
    /**
     * The type of this macro.
     */
    protected String type;
    
    /**
     * The path of a file or a package this macro was performed.
     */
    private String path;
    
    /**
     * The collection of raw macros that were recorded.
     */
    private List<Macro> rawMacros;
    
    /**
     * Creates an object storing information on a macro.
     * @param stime the time when the macro started
     * @param etime the time when the macro ended
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     */
    public Macro(long stime, long etime, String type, String path) {
        this.startTime = stime;
        this.endTime = etime;
        this.type = type;
        this.path = path;
    }
    
    /**
     * Creates an object storing information on a macro.
     * @param time the time when the macro started and ended 
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     */
    public Macro(long time, String type, String path) {
        this(time, time, type, path);
    }
    
    /**
     * Returns the time when this command started
     * @return the starting time of this macro
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Returns the time when this macro ended
     * @return the ending time of this macro
     */
    public long getEndTime() {
        return endTime;
    }
    
    /**
     * Sets the type of this macro
     * @param type the type of the macro
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Returns the type of this macro
     * @return the type of the macro
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the path of a file or a package this macro was performed
     * @return the path string
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Sets the collection of raw macros that were recorded.
     * @param macros the raw macros to be stored
     */
    public void setRawMacros(List<Macro> macros) {
        rawMacros = macros;
    }
    
    /**
     * Returns the collection of raw macros that were recorded.
     * @return raw macros
     */
    public List<Macro> getRawMacros() {
        return rawMacros;
    }
}
