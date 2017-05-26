/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.Time;

/**
 * Stores information on a normal editing operation.
 * @author Katsuhisa Maruyama
 */
public class NormalOperation extends TextOperation {
    
    /**
     * Defines the type of a normal operation.
     */
    public enum Type {
        EDIT, CUT, PASTE, UNDO, REDO, DIFF, OFF_EDIT, NO;
        
        /**
         * Checks the type of a normal operation.
         * @param str the string indicating the type 
         * @return the type of the file action, or <code>NO</code> if none
         */
        public static NormalOperation.Type parseType(String str) {
            if (StringComparator.isSameIgnoreCase(str, "edit")) {
                return EDIT;
            } else if (StringComparator.isSameIgnoreCase(str, "cut")) {
                return CUT;
            } else if (StringComparator.isSameIgnoreCase(str, "paste")) {
                return PASTE;
            } else if (StringComparator.isSameIgnoreCase(str,  "undo")) {
                return UNDO;
            } else if (StringComparator.isSameIgnoreCase(str, "redo")) {
                return REDO;
            } else if (StringComparator.isSameIgnoreCase(str, "diff")) {
                return DIFF;
            } else if (StringComparator.isSameIgnoreCase(str, "off_edit")) {
                return OFF_EDIT;
            } else 
            return NO;
        }
    }
    
    /**
     * The sort of the edit action for this operation.
     */
    private Type actionType = Type.EDIT;
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     */
    public NormalOperation(long time, int seq, String path, String author, int start, String itext, String dtext) {
        super(time, seq, path, author, start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     */
    public NormalOperation(long time, int seq, String path, int start, String itext, String dtext) {
        this(time, seq, path, AbstractOperation.getUserName(), start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     */
    public NormalOperation(long time, String path, String author, int start, String itext, String dtext) {
        super(time, path, author, start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     */
    public NormalOperation(long time, String path, int start, String itext, String dtext) {
        this(time, path, AbstractOperation.getUserName(), start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     * @param atype the sort of the edit action for this operation 
     */
    public NormalOperation(long time, int seq, String path, String author, int start, String itext, String dtext, Type atype) {
        this(time, seq, path, author, start, itext, dtext);
        this.actionType = atype;
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param developer the developer's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     * @param atype the sort of the edit action for this operation 
     */
    public NormalOperation(long time, int seq, String path, int start, String itext, String dtext, Type atype) {
        this(time, seq, path, AbstractOperation.getUserName(), start, itext, dtext, atype);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     * @param atype the sort of the edit action for this operation 
     */
    public NormalOperation(long time, String path, String author, int start, String itext, String dtext, Type atype) {
        this(time, 0, path, author, start, itext, dtext, atype);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the content of the text inserted by this operation
     * @param dtext the content of the text deleted by this operation
     * @param atype the sort of the edit action for this operation 
     */
    public NormalOperation(long time, String path, int start, String itext, String dtext, Type atype) {
        this(time, 0, path, AbstractOperation.getUserName(), start, itext, dtext, atype);
    }
    
    /**
     * Returns the sort of the edit action for this operation.
     * @return the string representing the edit action
     */
    public Type getActionType() {
        return actionType;
    }
    
    /**
     * Sets the sort of the edit action for this operation.
     * @param yupe the string representing the edit action
     */
    public void setActionType(Type type) {
        actionType = type;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.NORMAL;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof NormalOperation)) {
            return false;
        }
        
        NormalOperation nop = (NormalOperation)op;
        return super.equals(nop) && actionType == nop.getActionType();
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(Time.toUsefulFormat(time));
        buf.append(" " + actionType.toString());
        buf.append(" author=[" + author + "]");
        buf.append(" path=" + path + "]");
        buf.append(" offset=" + start);
        buf.append(" ins=[" + getText(insertedText) + "]");
        buf.append(" del=[" + getText(deletedText) + "]");
        
        return buf.toString();
    }
}
