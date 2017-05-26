/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;

/**
 * Stores information on a text operation.
 * @author Katsuhisa Maruyama
 */
public abstract class TextOperation extends AbstractOperation {
    
    /**
     * The leftmost offset of the text modified by this operation.
     */
    protected int start;
    
    /**
     * The contents of the text inserted by this operation.
     */
    protected String insertedText = "";
    
    /**
     * The contents of the text deleted by this operation.
     */
    protected String deletedText = "";
    
    /**
     * Creates an instance storing information on this operation.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the contents of the text inserted by this operation
     * @param dtext the contents of the text deleted by this operation
     */
    public TextOperation(long time, int seq, String path, String author, int start, String itext, String dtext) {
        super(time, seq, path, author);
        this.start = start;
        if (itext != null) {
            this.insertedText = itext;
        }
        if (dtext != null) {
            this.deletedText = dtext;
        }
    }
    
    /**
     * Creates an instance storing information on this operation.
     * @param time the time when this operation was performed
     * @param seq the sequence number of this operation
     * @param path the name of the file path on which this operation was performed
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the contents of the text inserted by this operation
     * @param dtext the contents of the text deleted by this operation
     */
    public TextOperation(long time, int seq, String path, int start, String itext, String dtext) {
        this(time, seq, path, AbstractOperation.getUserName(), start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param author the author's name
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the contents of the text inserted by this operation
     * @param dtext the contents of the text deleted by this operation
     */
    public TextOperation(long time, String path, String author, int start, String itext, String dtext) {
        this(time, 0, path, author, start, itext, dtext);
    }
    
    /**
     * Creates an instance storing information on this editing operation.
     * This constructor is called when the instance is created from the undo history of Eclipse.
     * @param time the time when this operation was performed
     * @param path the name of the file path on which this operation was performed
     * @param start the leftmost offset of the text modified by this operation
     * @param itext the contents of the text inserted by this operation
     * @param dtext the contents of the text deleted by this operation
     */
    public TextOperation(long time, String path, int start, String itext, String dtext) {
        this(time, path, AbstractOperation.getUserName(), start, itext, dtext);
    }
    
    /**
     * Returns the leftmost offset of the text modified by this operation.
     * @return the leftmost offset value of the modified text
     */
    public int getStart() {
        return start;
    }
    
    /**
     * Returns the contents of the text inserted by this operation.
     * @return the contents of the inserted text, or the empty string
     */
    public String getInsertedText() {
        return insertedText;
    }
    
    /**
     * Sets the contents of the text inserted by this operation.
     * @param text the contents of the inserted text
     */
    public void setInsertedText(String text) {
        insertedText = text;
    }
    
    /**
     * Returns the content of the text deleted by this operation.
     * @return the contents of the deleted text, or the empty string
     */
    public String getDeletedText() {
        return deletedText;
    }
    
    /**
     * Sets the contents of the text deleted by this operation.
     * @param text the contents of the deleted text
     */
    public void setDeletedText(String text) {
        deletedText = text;
    }
    
    /**
     * Tests if this operation did not any change of the text.
     * @return <code>true</code> if both the inserted and deleted texts are empty, otherwise <code>false</code>
     */
    public boolean isEmpty() {
        return insertedText.length() == 0 && deletedText.length() == 0;
        }
    
    /**
     * Tests if this operation inserted any text.
     * @return <code>true</code> if the inserted text is not empty but the deleted text is empty, otherwise <code>false</code>
     */
    public boolean isInsertion() {
        return insertedText.length() != 0 && deletedText.length() == 0;
    }
    
    /**
     * Tests if this operation inserted any text.
     * @return <code>true</code> if the deleted text is not empty but the inserted text is empty, otherwise <code>false</code>
     */
    public boolean isDeletion() {
        return insertedText.length() == 0 && deletedText.length() != 0;
    }
    
    /**
     * Tests if this operation inserted any text.
     * @return <code>true</code> if the inserted and deleted texts are not empty, otherwise <code>false</code>
     */
    public boolean isReplace() {
        return insertedText.length() != 0 && deletedText.length() != 0;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof TextOperation)) {
            return false;
        }
        
        TextOperation top = (TextOperation)op;
        return super.equals(top) && start == top.getStart() &&
               StringComparator.isSame(insertedText, top.getInsertedText()) &&
               StringComparator.isSame(deletedText, top.getDeletedText());
    }
}
