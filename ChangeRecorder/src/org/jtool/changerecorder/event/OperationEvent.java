/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.event;

import org.jtool.changerecorder.operation.IOperation;

/**
 * Manages an event indicating that the operation has performed.
 * @author Katsuhisa Maruyama
 */
public class OperationEvent {
    
    /**
     * Defines the type of this event.
     */
    public enum Type {
        OPERATION_ADDED, DEFAULT;
    }
    
    /**
     * The type of this event.
     */
    private OperationEvent.Type type = OperationEvent.Type.DEFAULT;
    
    /**
     * The operation sent by this operation event.
     */
    private IOperation operation;
    
    /**
     * Creates an instance containing information on this operation event.
     * @param type the type of this operation event
     * @param operation the operation sent by this operation event
     */
    public OperationEvent(OperationEvent.Type type, IOperation operation) {
        this.type = type;
        this.operation = operation;
    }
    
    /**
     * Creates an instance containing information on this operation event.
     * @param operation the operation sent by this operation event
     */
    public OperationEvent(IOperation operation) {
        this.operation = operation;
    }
    
    /**
     * Returns the type of this operation event.
     * @return the event type
     */
    public OperationEvent.Type getType() {
        return type;
    }
    
    /**
     * Returns the operation sent by this operation event.
     * @return the operation related to this operation event
     */
    public IOperation getOperation() {
        return operation;
    }
}
