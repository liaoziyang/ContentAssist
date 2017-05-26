/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

/**
 * Defines the interface used for accessing information on the all kinds of operations.
 * @author Katsuhisa Maruyama
 */
public interface IOperation {
    
    /**
     * Defines the type of an operation.
     */
    public enum Type {
        
        /**
         * The string indicating if this is a normal operation.
         */
        NORMAL,
        
        /**
         * The string indicating if this is a compounded operation.
         */
        COMPOUND,
        
        /**
         * The string indicating if this is a copy operation.
         */
        COPY,
        
        /**
         * The string indicating if this is a file operation.
         */
        FILE,
        
        /**
         * The string indicating if this is a menu operation.
         */
        MENU,
        
        /**
         * The string indicating if this is a resource change operation.
         */
        RESOURCE,
        
        /**
         * The string indicating if this is a null operation.
         */
        NULL;
    }
    
    /**
     * Returns the time when this operation was performed.
     * @return the time of the operation
     */
    public long getTime();
    
    /**
     * Returns the sequence number indicating the order of operations whose times are the same.
     * @return the sequence number of this operation
     */
    public int getSequenceNumber();
    
    /**
     * Returns the path name of the file on which this operation was performed.
     * @return the path name of the file
     */
    public String getFilePath();
    
    /**
     * Returns the name of a author who performs this operation.
     * @return the suthor's name
     */
    public String getAuthor();
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    public IOperation.Type getOperationType();
    
    /**
     * Tests if a given operation edits any text.
     * @return <code>true</code> if the operation is a text edit operation
     */
    public boolean isTextEditOperation();
    
    /**
     * Tests if a given operation changes any text.
     * @return <code>true</code> if the operation is a text change operation
     */
    public boolean isTextChangedOperation();
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    public boolean equals(IOperation op);
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end. 
     * @return the string for printing
     */
    public String toString();
}
