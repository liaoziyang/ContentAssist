/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a compound macro that contains macros.
 * @author Katsuhisa Maruyama
 */
public class CompoundMacro extends Macro {
    
    /**
     * The collection of macros contained in this compound macro.
     */
    private List<Macro> macros = new ArrayList<Macro>();
    
    /**
     * Creates an object storing information on a compound macro.
     * @param stime the time when the macro started
     * @param the type of the macro
     * @param path the path of a file or a package this macro was performed
     */
    public CompoundMacro(long stime, String type, String path) {
        super(stime, stime, type, path);
    }
    
    /**
     * Adds a macro into this compound macro.
     * @param macro the macro to be added
     */
    public void addMacro(Macro macro) {
        macros.add(macro);
    }
    
    /**
     * Removes a macro from this compound macro.
     * @param the index number of the macro to be removed
     */
    public void removeMacro(int index) {
        macros.remove(index);
    }
    
    /**
     * Removes a macro that will be canceled by a given macro.
     * @param macro the macro that cancels the macro stored in this compound macro
     * @return <code>true</code> if the cancellation succeeded, otherwise <code>false</code> 
     */
    public boolean cancelMacro(DocumentMacro macro) {
        int index = getIndexOfCorrespondingMacro(macro);
        if (index < 0) {
            return false;
        }
        
        macros.remove(index);
        return true;
    }
    
    /**
     * Returns the collection of macros contained in this compound macro.
     * @return the the collection of the contained macros
     */
    public List<Macro> getMacros() {
        return macros;
    }
    
    /**
     * Sets the starting and ending times.
     */
    public void setTimes() {
        for (Macro m : macros) {
            if (m.getStartTime() < startTime) {
                startTime = m.getStartTime();
            }
            if (m.getEndTime() > endTime) {
                endTime = m.getEndTime();
            }
        }
    }
    
    /**
     * Obtains the index number of the macro corresponding to a given macro.
     * @param macro the given macro
     * @return the index number of the corresponding macro
     */
    private int getIndexOfCorrespondingMacro(DocumentMacro macro) {
        for (int i = 0; i < macros.size(); i++) {
            Macro m = macros.get(i);
            if (m instanceof DocumentMacro) {
                DocumentMacro dm = (DocumentMacro)m;
                if (dm.getStart() == macro.getStart() &&
                    dm.getInsertedText().compareTo(macro.getDeletedText()) == 0 &&
                    dm.getDeletedText().compareTo(macro.getInsertedText()) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "COMP(" + getType() + ") = " + getPath() + " " +
               getStartTime() + "-" + getEndTime();
    }
}
