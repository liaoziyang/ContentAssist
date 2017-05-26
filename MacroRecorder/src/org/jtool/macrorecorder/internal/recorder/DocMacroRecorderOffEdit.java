/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import org.jtool.macrorecorder.recorder.Recorder;
import org.jtool.macrorecorder.util.EditorUtilities;
import org.jtool.macrorecorder.macro.DocumentMacro;
import org.jtool.macrorecorder.macro.ExecutionMacro;
import org.jtool.macrorecorder.macro.TriggerMacro;
import org.jtool.macrorecorder.macro.ResourceMacro;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;

/**
 * Records document macros related to a file.
 * @author Katsuhisa Maruyama
 */
public class DocMacroRecorderOffEdit extends DocMacroRecorder {
    
    /**
     * A file related to recorded macros.
     */
    private IFile file;
    
    /**
     * The document of a file.
     */
    private IDocument doc;
    
    /**
     * Creates an object that records document macros related to a file.
     * @param file the file
     * @param recorder a recorder that sends macro events
     */
    public DocMacroRecorderOffEdit(IFile file, Recorder recorder) {
        super(EditorUtilities.getInputFilePath(file), recorder);
        
        this.file = file;
        this.doc = EditorUtilities.getDocument(file);
    }
    
    /**
     * Starts the recording of document macros.
     */
    public void start() {
        if (file == null) {
            return;
        }
        
        DocumentManager.register(doc, null, documentManager);
        
        preCode = doc.get();
        
        super.start();
    }
    
    /**
     * Stops the recording of macros.
     */
    public void stop() {
        if (file == null) {
            return;
        }
        
        DocumentManager.unregister(doc, null, documentManager);
        
        super.stop();
    }
    
    /**
     * Records a document macro and its compressed macro.
     * @param macro the document macro
     */
    protected void recordDocumentMacro(DocumentMacro macro) {
        super.recordDocumentMacro(macro);
    }
    
    /**
     * Records a command execution macro.
     * @param macro the command execution macro
     */
    protected void recordExecutionMacro(ExecutionMacro macro) {
        super.recordExecutionMacro(macro);
        recordRawMacro(macro);
    }
    
    /**
     * Records a trigger macro.
     * @param macro the trigger macro
     */
    protected void recordTriggerMacro(TriggerMacro macro) {
        super.recordTriggerMacro(macro);
    }
    
    /**
     * Records a resource change macro.
     * @param macro the resource change macro
     */
    protected void recordResourceMacro(ResourceMacro macro) {
        super.recordResourceMacro(macro);
    }
    
    /**
     * Records a compressed macro into the operation history and its original one in .
     * @param macro a document macro
     */
    protected void recordUndoRedoMacro(DocumentMacro macro) {
        super.recordUndoRedoMacro(macro);
    }
    
    /**
     * Obtains the current contents of a file under recording.
     * @return the contents of source code, or <code>null</code> if source code does not exist
     */
    protected String getCurrentCode() {
        IDocument doc = EditorUtilities.getDocument(file);
        if (doc != null) {
            return doc.get();
        }
        return null;
    }
}
