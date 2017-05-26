/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.recorder;

import org.jtool.macrorecorder.macro.Macro;

/**
 * Manages an event containing a macro.
 * @author Katsuhisa Maruyama
 */
public class MacroEvent {
    
    /**
     * The value indicating general macros.
     */
    public static int GENERIC_MACRO = 1;
    
    /**
     * The value indicating raw macros per keystroke.
     */
    public static int RAW_MACRO = 2;
    
    /**
     * A macro sent to listeners.
     */
    private Macro macro;
    
    /**
     * The type of a macro sent to listeners.
     */
    private int type;
    
    /**
     * Creates an object containing a macro.
     * @param macro the macro sent to listeners
     */
    public MacroEvent(int type, Macro macro) {
        this.type = type;
        this.macro = macro;
    }
    
    /**
     * Returns the type of an event that listeners receive.
     * @return the type of the received event
     */
    public int getEventType() {
        return type;
    }
    
    /**
     * Returns the macro of an event that listeners receive.
     * @return the macro contained in the received event
     */
    public Macro getMacro() {
        return macro;
    }
}
