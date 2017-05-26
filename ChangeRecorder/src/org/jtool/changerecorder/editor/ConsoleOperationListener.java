/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.editor;

import org.jtool.changerecorder.event.OperationEvent;
import org.jtool.changerecorder.event.OperationEventListener;

/**
 * Displays operations recorded on editors.
 * @author Katsuhisa Maruyama
 */
public class ConsoleOperationListener implements OperationEventListener {
    
    /**
     * Receives an operation event when operation history was updated.
     * @param evt the received event
     */
    @Override
    public void historyNotification(OperationEvent evt) {
        System.out.println(evt.getOperation().toString());
    }

}
