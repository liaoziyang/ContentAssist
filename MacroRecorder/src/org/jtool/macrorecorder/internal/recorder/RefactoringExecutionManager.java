/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import org.jtool.macrorecorder.macro.Macro;
import org.jtool.macrorecorder.macro.TriggerMacro;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringHistoryEvent;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryService;

/**
 * Manages refactoring events.
 * @author Katsuhisa Maruyama
 */
public class RefactoringExecutionManager implements IRefactoringExecutionListener, IRefactoringHistoryListener {
    
    /**
     * A recorder that records menu actions.
     */
    private MenuMacroRecorder recorder;
    
    /**
     * Creates an object that records refectoring execution events.
     * @param recorder a recorder that records menu actions
     */
    public RefactoringExecutionManager(MenuMacroRecorder recorder) {
        this.recorder = recorder;
    }
    
    /**
     * Registers a refactoring execution manager with the refactoring history service.
     * @param rm the refactoring execution manager
     */
    public static void register(RefactoringExecutionManager rm) {
        IRefactoringHistoryService rs = RefactoringCore.getHistoryService();
        if (rs != null) {
            rs.addExecutionListener(rm);
            // rs.addHistoryListener(rm);
        }
    }
    
    /**
     * Unregisters a refactoring execution manager with the refactoring history service.
     * @param rm the refactoring execution manager
     */
    public static void unregister(RefactoringExecutionManager rm) {
        IRefactoringHistoryService rs = RefactoringCore.getHistoryService();
        if (rs != null) {
            rs.removeExecutionListener(rm);
            // rs.removeHistoryListener(rm);
        }
    }
    
    /**
     * Receives an event when a refactoring execution event happened.
     * @param event the refactoring execution event
     */
    @Override
    public void executionNotification(RefactoringExecutionEvent event) {
        long time = Time.getCurrentTime();
        Macro macro = recorder.getParentMacro();
        String path = null;
        if (macro != null) {
            path = macro.getPath();
        }
        
        String commandId = event.getDescriptor().getDescription();
        if (event.getEventType() == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
            TriggerMacro trigger = new TriggerMacro(time, commandId, path, TriggerMacro.Kind.BEGIN);
            recorder.recordTriggerMacro(trigger);
            
        } else if (event.getEventType() == RefactoringExecutionEvent.PERFORMED) {
            TriggerMacro trigger = new TriggerMacro(time, commandId, path, TriggerMacro.Kind.END);
            path = null;
            recorder.recordTriggerMacro(trigger);
            
            recorder.setParentMacro(null);
        }
    }
    
    /**
     * Receives an event when a refactoring history event happened.
     * @param event the refactoring history event
     */
    @Override
    public void historyNotification(RefactoringHistoryEvent event) {
    }
}
