/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.recorder;

/**
 * Defines the listener interface for receiving a macro event.
 * @author Katsuhisa Maruyama
 */
public interface MacroListener {
    
    /**
     * Receives a macro event when a new macro is added.
     * @param evt the macro event
     */
    public void macroAdded(MacroEvent evt);
    
    /**
     * Receives a macro event when a document is changed.
     * @param evt the raw macro event
     */
    public void documentChanged(MacroEvent evt);
}
