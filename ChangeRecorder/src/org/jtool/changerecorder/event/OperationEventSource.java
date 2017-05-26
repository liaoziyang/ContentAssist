/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.event;

import org.jtool.changerecorder.operation.IOperation;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages a source that send an operation event.
 * @author Katsuhisa Maruyama
 */
public class OperationEventSource {
    
    /**
     * The collection of listeners that receives operation events.
     */
    private List<OperationEventListener> listeners = new ArrayList<OperationEventListener>();
    
    /**
     * Creates an object that performs as an event source.
     */
    public OperationEventSource() {
    }
    
    /**
     * Adds a listener in order to receive an operation event from this source.
     * @param listener the event listener to be added
     */
    public void addOperationEventListener(OperationEventListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener which no longer receives an operation event from this source.
     * @param listener the event listener to be removed
     */
    public void removeOperationEventListener(OperationEventListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Sends an operation event to all the listeners.
     * @param evt the operation event.
     */
    protected void notify(IOperation op) {
        OperationEvent evt = new OperationEvent(OperationEvent.Type.OPERATION_ADDED, op);
        for (OperationEventListener listener : listeners) {
            listener.historyNotification(evt);
        }
    }
}
