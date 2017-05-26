/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import org.jtool.macrorecorder.macro.DocumentMacro;
import org.jtool.macrorecorder.macro.TriggerMacro;
import org.jtool.macrorecorder.macro.CancelMacro;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoListener;
import org.eclipse.text.undo.DocumentUndoEvent;
import org.eclipse.text.undo.IDocumentUndoManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Manages document events.
 * @author Katsuhisa Maruyama
 */
public class DocumentManager implements IDocumentListener, IDocumentUndoListener, Listener {
    
    /**
     * A recorder that records macros.
     */
    private DocMacroRecorder recorder;
    
    /**
     * The contents of the document inserted by this macro.
     */
    private String insertedText;
    
    /**
     * The contents of the document deleted by this macro.
     */
    private String deletedText;
    
    /**
     * The kinds of a macro.
     */
    private enum UndoRedoKind {
        UNDO, REDO, NO;
    }
    
    /**
     * The object that indicates the kind of undo/redo macro.
     */
    private UndoRedoKind kind;
    
    /**
     * Creates an object that records document events.
     * @param recorder a recorder that records macros
     */
    public DocumentManager(DocMacroRecorder recorder) {
        this.recorder = recorder;
    }
    
    /**
     * Registers a document manager with an editor.
     * @param doc the document to be managed
     * @param st the styled text of the editor
     * @param dm the document manager
     */
    public static void register(IDocument doc, StyledText st, DocumentManager dm) {
        if (doc != null) {
            doc.addDocumentListener(dm);
            
            DocumentUndoManagerRegistry.connect(doc);
            IDocumentUndoManager undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(doc);
            if (undoManager != null) {
                undoManager.addDocumentUndoListener(dm);
            }
        }
        
        if (st != null) {
            st.addListener(SWT.KeyDown, dm);
            st.addListener(SWT.MouseDown, dm);
            st.addListener(SWT.MouseDoubleClick, dm);
        }
    }
    
    /**
     * Unregisters a document manager with an editor.
     * @param doc the document to be managed
     * @param st the styled text of the editor
     * @param dm the document manager
     */
    public static void unregister(IDocument doc, StyledText st, DocumentManager dm) {
        if (doc != null) {
            doc.removeDocumentListener(dm);
            
            IDocumentUndoManager undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(doc);
            DocumentUndoManagerRegistry.disconnect(doc);
            if (undoManager != null) {
                undoManager.removeDocumentUndoListener(dm);
            }
        }
        
        if (st != null) {
            st.removeListener(SWT.KeyDown, dm);
            st.removeListener(SWT.MouseDown, dm);
            st.removeListener(SWT.MouseDoubleClick, dm);
        }
    }
    
    /**
     * Receives a document event will be performed.
     * @param event the document event describing the document change
     */
    @Override
    public void documentAboutToBeChanged(DocumentEvent event) {
        insertedText = event.getText();
        deletedText = "";
        if (event.getLength() > 0) {
            IDocument doc = event.getDocument();
            
            try {
                deletedText = doc.get(event.getOffset(), event.getLength());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        
        if (insertedText.length() == 0 && deletedText.length() == 0) {
            return;
        }
        
        long time = Time.getCurrentTime();
        String path = recorder.getPath();
        
        if (kind == UndoRedoKind.UNDO) {
            DocumentMacro macro;
            if (recorder.getParentMacro() == null) {
                macro = new DocumentMacro(time, "Undo", path, event.getOffset(), insertedText, deletedText);
            } else {
                macro = new CancelMacro(time, "Undo", path, event.getOffset(), insertedText, deletedText);
            }
            recorder.recordUndoRedoMacro(macro);
            
        } else if (kind == UndoRedoKind.REDO) {
            DocumentMacro macro;
            if (recorder.getParentMacro() == null) {
                macro = new DocumentMacro(time, "Redo", path, event.getOffset(), insertedText, deletedText);
            } else {
                macro = new CancelMacro(time, "Redo", path, event.getOffset(), insertedText, deletedText);
            }
            recorder.recordUndoRedoMacro(macro);
            
        } else {
            DocumentMacro macro = new DocumentMacro(time, "Typing", path, event.getOffset(), insertedText, deletedText);
            recorder.recordDocumentMacro(macro);
        }
    }
    
    /**
     * Receives a document event has been performed.
     * @param event the document event describing the document change
     */
    @Override
    public void documentChanged(DocumentEvent event) {
    }
    
    /**
     * Receives a document undo event when the document is involved in an undo-related change.
     * @param event the document undo event describing the particular notification
     */
    @Override
    public void documentUndoNotification(DocumentUndoEvent event) {
        long time = Time.getCurrentTime();
        String path = recorder.getPath();
        
        int type = event.getEventType();
        if (type >= 16) {
            type = type - 16;
        }
        
        if (type == DocumentUndoEvent.ABOUT_TO_UNDO) {
            if (recorder.getParentMacro() == null) {
                TriggerMacro trigger = new TriggerMacro(time, "Undo", path, TriggerMacro.Kind.BEGIN);
                recorder.recordTriggerMacro(trigger);
            }
            kind = UndoRedoKind.UNDO;
            
        } else if (type == DocumentUndoEvent.ABOUT_TO_REDO) {
            if (recorder.getParentMacro() == null) {
                TriggerMacro trigger = new TriggerMacro(time, "Redo", path, TriggerMacro.Kind.BEGIN);
                recorder.recordTriggerMacro(trigger);
            }
            kind = UndoRedoKind.REDO;
            
        } else if (type == DocumentUndoEvent.UNDONE) {
            if (recorder.getParentMacro() == null) {
                TriggerMacro trigger = new TriggerMacro(time, "Undo", path, TriggerMacro.Kind.END);
                recorder.recordTriggerMacro(trigger);
            }
            kind = UndoRedoKind.NO;
            
        } else if (type == DocumentUndoEvent.REDONE) {
            if (recorder.getParentMacro() == null) {
                TriggerMacro trigger = new TriggerMacro(time, "Redo", path, TriggerMacro.Kind.END);
                recorder.recordTriggerMacro(trigger);
            }
            kind = UndoRedoKind.NO;
        }
    }
    
    /**
     * Receives an event when the registered event occurs.
     * @param event the event which occurred
     */
    @Override
    public void handleEvent(Event event) {
        boolean cursorMoved = false;
        if (event.type == SWT.KeyDown) {
            cursorMoved = cursorMoved(event);
            
        } else if (event.type == SWT.MouseDown || event.type == SWT.MouseDoubleClick) {
            cursorMoved = true;
        }
        
        if (cursorMoved) {
            long time = Time.getCurrentTime();
            String path = recorder.getPath();
            
            String commandId = "Cursor.position.change";
            TriggerMacro trigger = new TriggerMacro(time, commandId, path, TriggerMacro.Kind.CURSOR_CHANGE);
            recorder.recordTriggerMacro(trigger);
            
            recorder.setParentMacro(null);
        }
    }
    
    /**
     * Tests if a given key event may move the current cursor position.
     * @param event the key event
     * @return <code>true</code> if the key event may move the current cursor position, otherwise <code>false</code>
     */
    private boolean cursorMoved(Event event) {
        final int key = (SWT.KEY_MASK & event.keyCode);
        switch (key) {
            case SWT.ARROW_DOWN:
            case SWT.ARROW_LEFT:
            case SWT.ARROW_RIGHT: 
            case SWT.ARROW_UP: 
            case SWT.HOME:
            case SWT.END: 
            case SWT.PAGE_DOWN:
                return true;
        }
        return false;
    }
}
