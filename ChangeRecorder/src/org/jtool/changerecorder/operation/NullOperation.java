/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

/**
 * Stores information on a null operation.
 * This operation does not usually appear.
 * @author Katsuhisa Maruyama
 */
public class NullOperation extends AbstractOperation {
    
    /**
     * The cache that stores the original operation.
     */
    private IOperation originalOperation = null;
    
    /**
     * Creates an instance storing information on this null operation.
     */
    public NullOperation() {
        super(-1, null, null);
    }
    
    /**
     * Creates an instance storing information on this null operation. 
     * @param original the original operation to be stored
     */
    public NullOperation(IOperation original) {
        super(-1, null, null);
        originalOperation = original;
    }
    
    /**
     * Returns the stored original operation
     * @return the original operation
     */
    public IOperation getOriginalOperation() {
        return originalOperation;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.NULL;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return always <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        return false;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    @Override
    public String toString() {
        return "NULL";
    }
}
