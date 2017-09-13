/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser;

import jp.ac.ritsumei.cs.fse.contentassist.Activator;
import jp.ac.ritsumei.cs.fse.contentassist.entity.ApplyOperation;

import org.jtool.changerecorder.event.OperationEvent;
import org.jtool.changerecorder.event.OperationEventListener;
import org.jtool.changerecorder.operation.TextOperation;

/**
 * Displays operations recorded on editors.
 * @author liaoziyang
 */
public class ConsoleOperationListener2 implements OperationEventListener {

    /**
     * Receives an operation event when operation history was updated.
     * @param evt the received event
     */
    HistoryRecorder hr = new HistoryRecorder();
    public static TextOperation ope;
    public void historyNotification(OperationEvent evt) {
    	try {
        	ConsoleOperationListener2.ope = (TextOperation) evt.getOperation();
    	}
    	catch(Exception e) {
    		
    	}
    	for (ApplyOperation ao : Activator.applyoperationlist) {
    		ao.update(ope.getStart(), ope.getDeletedText(), ope.getInsertedText());
    		System.out.println(ao.toString());
    	}	
    }
  }

