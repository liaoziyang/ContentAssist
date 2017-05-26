/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.Time;
import java.util.List;
import java.util.ArrayList;

/**
 * Stores information on a compound operation enclosing multiple operations.
 * @author Katsuhisa Maruyama
 */
public class CompoundOperation extends AbstractOperation {
    
    /**
     * The array list of operations contained in this operation.
     */
    private List<? extends IOperation> operations = new ArrayList<IOperation>();
    
    /**
     * The label indicating the name of this operation.
     */
    protected String label;
    
    /**
     * Creates an instance storing information on this compound operation.
     * @param time the time when this operation was performed
     * @param author the author's name
     * @param ops the operations contained in this operation
     * @param label the label indicating the name of the undo operation
     */
    public CompoundOperation(long time, String author, List<? extends IOperation> ops, String label) {
        super(time, "", author);
        this.operations = ops;
        this.label = label;
    }
    
    /**
     * Creates an instance storing information on this compound operation.
     * @param time the time when this operation was performed
     * @param ops the operations contained in this operation
     * @param label the label indicating the name of the undo operation
     */
    public CompoundOperation(long time, List<? extends IOperation> ops, String label) {
        this(time, AbstractOperation.getUserName(), ops, label);
    }
    
    /**
     * Returns the array of operations contained in this operation.
     * @return the contained operations
     */
    public List<? extends IOperation> getOperations() {
        return operations;
    }
    
    /**
     * Returns the label indicating the name of the undo operation.
     * @return the label string
     */
    public String getLabel() {
        return label;
    }
    
    /** 
     * Returns the array list of operations dangling on this operation.
     * @return the dangling editing operations
     */
    public List<? extends IOperation> getLeaves() {
        List<IOperation> leaves = new ArrayList<IOperation>();
        for (IOperation op : operations) {
            if (op.getOperationType() == IOperation.Type.COMPOUND) {
                CompoundOperation cop = (CompoundOperation)op;
                leaves.addAll(cop.getLeaves());
            } else {
                leaves.add(op);
            }
        }
        return leaves;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.COMPOUND;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof CompoundOperation)) {
            return false;
        }
        
        CompoundOperation cop = (CompoundOperation)op;
        if (!super.equals(cop) || !StringComparator.isSame(label, cop.getLabel()) ||
            operations.size() != cop.getOperations().size()) {
            return false;
        }
        
        for (int idx = 0; idx < operations.size(); idx++) {
            if (!operations.get(idx).equals(cop.getOperations().get(idx))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append(Time.toUsefulFormat(time));
        buf.append(" label=[" + label + "]\n");
        
        for (IOperation op : operations) {
            buf.append("  " + op.toString());
            buf.append("\n");
        }
        buf.append("}");
        
        return buf.toString();
    }
}
