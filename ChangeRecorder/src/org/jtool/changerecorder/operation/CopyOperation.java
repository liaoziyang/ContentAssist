/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.Time;

/**
 * Stores information on a copy operation.
 * @author Katsuhisa Maruyama
 */
public class CopyOperation extends AbstractOperation {
    
    /**
     * The leftmost offset of the text modified by this editing operation.
     */
    protected int start;
    
    /**
     * The content of the text copied by this editing operation.
     */
    protected String copiedText;
    
    /**
     * Creates an instance storing information on this copy operation.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param ctext the content of the text copied by this operation
     */
    public CopyOperation(long time, String path, String author, int start, String ctext) {
        super(time, path, author);
        this.start = start;
        this.copiedText = ctext;
    }
    
    /**
     * Creates an instance storing information on this copy operation.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param start the leftmost offset of the text modified by this operation
     * @param ctext the content of the text copied by this operation
     */
    public CopyOperation(long time, String path, int start, String ctext) {
        this(time, path, AbstractOperation.getUserName(), start, ctext);
    }
    
    /**
     * Returns the leftmost offset of the text copied by this operation.
     * @return the leftmost offset value of the modified text
     */
    public int getStart() {
        return start;
    }
    
    /**
     * Returns the content of the text copied by this operation.
     * @return the content of the copied text, or the empty string
     */
    public String getCopiedText() {
        return copiedText;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.COPY;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof CopyOperation)) {
            return false;
        }
        
        CopyOperation cop = (CopyOperation)op;
        return super.equals(cop) &&
               start == cop.getStart() && StringComparator.isSame(copiedText, cop.getCopiedText());
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(Time.toUsefulFormat(time));
        buf.append(" author=[" + author + "]");
        buf.append(" path=" + path + "]");
        buf.append(" offset=" + start);
        buf.append(" copied=[" + getText(copiedText) + "]\n");
        
        return buf.toString();
    }
}
