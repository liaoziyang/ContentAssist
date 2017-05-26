/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.Time;

/**
 * Stores information on a menu operation.
 * @author Katsuhisa Maruyama
 */
public class MenuOperation extends AbstractOperation {
    
    /**
     * The label indicating the contents of this operation.
     */
    protected String label;
    
    /**
     * Provides the constructor for creating an instance storing information on this operation.
     * @param time the time when this operation was performed
     * @param path the name of the file on which this operation was performed
     * @param author the author's name
     * @param the label indicating the name of the menu operation
     */
    public MenuOperation(long time, String path, String author, String label) {
        super(time, path, author);
        this.label = label;
    }
    
    /**
     * Provides the constructor for creating an instance storing information on this operation.
     * @param time the time when this operation was performed
     * @param path the name of the file on which this operation was performed
     * @param the label indicating the name of the menu operation
     */
    public MenuOperation(long time, String path, String label) {
        this(time, path, AbstractOperation.getUserName(), label);
    }
    
    /**
     * Returns the label indicating the name of the menu operation.
     * @return the label string
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.MENU;
    }
    
	/**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof MenuOperation)) {
            return false;
        }
        
        MenuOperation mop = (MenuOperation)op;
        return super.equals(mop) && StringComparator.isSame(label, mop.getLabel());
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end. 
     * @return the string for printing
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(Time.toUsefulFormat(time));
        buf.append(" MENU ");
        buf.append(" label=[" + label + "]");
        buf.append(" autor=[" + author + "]");
        buf.append(" path=" + path);
        
        return buf.toString();
    }
}
