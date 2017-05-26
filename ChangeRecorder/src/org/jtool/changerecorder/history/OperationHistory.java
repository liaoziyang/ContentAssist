/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.history;

import org.jtool.changerecorder.Activator;
import org.jtool.changerecorder.operation.CompoundOperation;
import org.jtool.changerecorder.operation.IOperation;
import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.XmlFileStream;
import org.w3c.dom.Document;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Stores information on operation history.
 * @author Katsuhisa Maruyama
 */
public class OperationHistory {
    
    /**
     * The editing operations correctly stored into the operation history.
     */
    protected List<IOperation> operations;
    
    /**
     * Creates an instance storing operation history.
     */
    public OperationHistory() {
        this.operations = new ArrayList<IOperation>();
    }
    
    /**
     * Creates an instance storing operation history.
     * @param ops the collection of the operations to be stored
     */
    public OperationHistory(List<IOperation> ops) {
        this.operations = ops;
    }
    
    /**
     * Obtains all the operations stored into this operation history.
     * @return the collection of the stored operations
     */
    public List<IOperation> getOperations() {
        return operations;
    }
    
    /**
     * Sets all the operations stored into this operation history.
     * @param the collection of operations to be stored
     */
    public void setOperations(List<IOperation> ops) {
        operations = ops;
    }
    
    /**
     * Returns the size of this operation history.
     * @return the size of operations stored in the operation history
     */
    public int size() {
        return operations.size();
    }
    
    /**
     * Returns the operation at the specified position in this operation history.
     * @param idx the index of the operation to be returned
     * @return the operation at the specified position 
     */
    public IOperation getOperation(int idx) {
        return operations.get(idx);
    }
    
    /**
     * Adds an operation into this operation history.
     * @param op the operation to be added
     */
    public void add(IOperation op) {
        operations.add(op);
    }
    
    /**
     * Merges a given operation history into this operation history.
     * @param history the operation history to be merged
     */
    public void merge(OperationHistory history) {
        operations.addAll(history.getOperations());
    }
    
    /**
     * Deletes all the operations stored in this history.
     */
    public void clear() {
        operations.clear();
    }
    
    /**
     * Sorts the operations stored in this operation history.
     */
    public void sort() {
        sort(operations);
    }
    
    /**
     * Removes operations related to a specified file from this operation history.
     * @param path the path of the specified file,
     * or <code>null</code> if operations not having a relation to any file are removed
     */
    public void remove(String path) {
        for (int i = 0; i < operations.size(); i++) {
            IOperation op = operations.get(i);
            if (isRelatedTo(op, path)) {
                operations.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Obtains operations in this operation history, which are related to a specified file.
     * @param path the path of the specified file
     * @return the operation history storing the found operations.
     * The size of the history is zero if no operations was found
     */
    public OperationHistory getHistory(String path) {
        ArrayList<IOperation> ops = new ArrayList<IOperation>();
        if (path != null) {
            for (IOperation operation : operations) {
                if (isRelatedTo(operation, path)) {
                    ops.add(operation);
                }
            }
        }
        
        OperationHistory history = new OperationHistory(ops);
        return history;
    }
    
    /**
     * Tests if a given operation is related to a specified file.
     * @param op the given operation to be checked
     * @param path the path of the specified file
     * @return <code>true</code> if the operation is related to the file, otherwise <code>false</code>
     */
    private boolean isRelatedTo(IOperation op, String path) {
        if (op.getOperationType() == IOperation.Type.NORMAL ||
            op.getOperationType() == IOperation.Type.FILE ||
            op.getOperationType() == IOperation.Type.COPY ||
            op.getOperationType() == IOperation.Type.MENU ||
            op.getOperationType() == IOperation.Type.RESOURCE) {
            return StringComparator.isSame(op.getFilePath(), path);
            
        } else if (op.getOperationType() == IOperation.Type.COMPOUND) {
            CompoundOperation cop = (CompoundOperation)op;
            for (IOperation o : cop.getLeaves()) {
                if (StringComparator.isSame(o.getFilePath(), path)) {
                    return true;
                }
            }
            return false;
        }
        
        return false;
    }
    
    /**
     * Obtains the first operation from this operation history.
     * @return the first operation, or <code>null</code> if none
     */
    public IOperation getFirstOperation() {
        if (operations.size() > 0) {
            return operations.get(0);
        }
        return null;
    }
    
    /**
     * Obtains the last operation from this operation history.
     * @return the last operation, or <code>null</code> if none
     */
    public IOperation getLastOperation() {
        if (operations.size() > 0) {
            return operations.get(operations.size() - 1);
        }
        return null;
    }
    
    /**
     * Writes this operation history into its history file.
     * @param the encoding of a text to be written
     * @return <code>true</code> if the operation history might be successfully written, otherwise <code>false</code>
     */
     public boolean write(String path, String encoding) {
        if (size() == 0) {
            return false;
        }
        
        Document doc = Operation2Xml.convert(this);
        XmlFileStream.write(doc, path, encoding);
        
        return true;
    }
    
     /**
      * Returns the directory path of the plug-in's workspace, which contains operation history. 
      * @return the the directory into which the operation history is stored
      */
     public static String getOperationHistoryDirPath() {
         return Activator.getPlugin().getOperationHistoryDirPath();
     }
     
    /**
     * Sorts the operations in time order.
     * @param the collection of the operations to be sorted
     */
    private static void sort(List<IOperation> ops) {
        Collections.sort(ops, new Comparator<IOperation>() {
            
            public int compare(IOperation o1, IOperation o2) {
                long time1 = o1.getTime();
                long time2 = o2.getTime();
                
                if (time1 > time2) {
                    return 1;
                } else if (time1 < time2) {
                    return -1;
                    
                } else {
                    int seq1 = o1.getSequenceNumber();
                    int seq2 = o2.getSequenceNumber();
                    
                    if (seq1 > seq2) {
                        return 1;
                    } else if (seq1 < seq2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }
    
    /**
     * Returns the string for printing.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("+--------------------------+\n");
        for (IOperation item : operations) {
            buf.append(item.toString());
            buf.append("\n");
        }
        buf.append("+--------------------------+\n");
        
        return buf.toString();
    }
}
