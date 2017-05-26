/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.event;

/**
 * Defines the listener interface for receiving an operation event.
 * @author Katsuhisa Maruyama
 */
public interface OperationEventListener {
    
    /**
     * Receives an operation event when operation history was updated.
     * @param evt the received event
     */
    public void historyNotification(OperationEvent evt);
}
