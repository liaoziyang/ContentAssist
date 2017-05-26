/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import org.jtool.macrorecorder.recorder.MacroCompressor;
import org.jtool.macrorecorder.recorder.Recorder;
import org.jtool.macrorecorder.util.EditorUtilities;
import org.jtool.macrorecorder.macro.DocumentMacro;
import org.jtool.macrorecorder.macro.ExecutionMacro;
import org.jtool.macrorecorder.macro.TriggerMacro;
import org.jtool.macrorecorder.macro.ResourceMacro;
import org.jtool.macrorecorder.macro.CopyMacro;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.text.IDocument;

/**
 * Records document macros performed on the editor.
 * @author Katsuhisa Maruyama
 */
public class DocMacroRecorderOnEdit extends DocMacroRecorder {
    
    /**
     * An editor on which document macros are recorded.
     */
    private IEditorPart editor;
    
    /**
     * The document of a file.
     */
    private IDocument doc;
    
    /**
     * A compressor that compresses macros.
     */
    private MacroCompressor compressor;
    
    /**
     * The styled text of an editor.
     */
    private StyledText styledText;
    
    /**
     * Creates an object that records document macros performed on an editor.
     * @param editor the editor
     * @param recorder a recorder that sends macro events
     * @param compressor a compressor that compresses macros
     */
    public DocMacroRecorderOnEdit(IEditorPart editor, Recorder recorder, MacroCompressor compressor) {
        super(EditorUtilities.getInputFilePath(editor), recorder);
        
        this.editor = editor;
        this.doc = EditorUtilities.getDocument(editor);
        this.compressor = compressor;
        this.styledText = EditorUtilities.getStyledText(editor);
    }
    
    /**
     * Starts the recording of document macros.
     */
    public void start() {
        if (editor == null) {
            return;
        }
        
        DocumentManager.register(doc, styledText, documentManager);
        
        preCode = doc.get();
        
        super.start();
    }
    
    /**
     * Stops the recording of macros.
     */
    public void stop() {
        if (editor == null) {
            return;
        }
        
        DocumentManager.unregister(doc, styledText, documentManager);
        
        super.stop();
    }
    
    /**
     * Records a document macro and its compressed macro.
     * @param macro the document macro
     */
    protected void recordDocumentMacro(DocumentMacro macro) {
        boolean isCutPaste = setCutPasteMacro(macro);
        recordRawMacro(macro);
        // System.out.println("MACRO = " + macro);
        
        if (isCutPaste) {
            dumpMacros(macro);
            return;
        }
        
        if (compressor.canCombine(macro)) {
            DocumentMacro newMacro = compressor.combine(lastDocumentMacro, macro);
            if (newMacro != null) {
                lastDocumentMacro = newMacro;
            } else {
                dumpLastDocumentMacro();
                lastDocumentMacro = macro;
            }
        } else {
            dumpMacros(macro);
        }
    }
    
    /**
     * Tests if a macro indicates the cut or paste and sets its type according to its result.
     * @param macro a macro that might be a cut or paste one
     * @return <code>true</code> if a macro indicates the cut or paste, otherwise <code>false</code>
     */
    boolean setCutPasteMacro(DocumentMacro macro) {
        if (lastRawMacro == null) {
            return false;
        }
        
        if (lastRawMacro instanceof ExecutionMacro) {
            ExecutionMacro emacro = (ExecutionMacro)lastRawMacro;
            if (emacro.getCommandId().compareTo("org.eclipse.ui.edit.cut") == 0) {
                macro.setType("Cut");
                return true;
            } else if (emacro.getCommandId().compareTo("org.eclipse.ui.edit.paste") == 0) {
                macro.setType("Paste");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Records a command execution macro.
     * @param macro the command execution macro
     */
    protected void recordExecutionMacro(ExecutionMacro macro) {
        super.recordExecutionMacro(macro);
        
        if (styledText == null) {
            return;
        }
        
        if (macro.getCommandId().compareTo("org.eclipse.ui.edit.copy") == 0 ||
            macro.getCommandId().compareTo("org.eclipse.jdt.ui.edit.text.java.copy.qualified.name") == 0) {
            int offset = styledText.getSelectionRange().x;
            String text = styledText.getSelectionText();
            
            long time = Time.getCurrentTime();
            CopyMacro cmacro = new CopyMacro(time, "Copy", macro.getPath(), offset, text);
            
            recordRawMacro(cmacro);
            dumpMacros(cmacro);
            
        } else if (macro.getCommandId().compareTo("org.eclipse.ui.edit.delete") == 0) {
            macro.setType("Delete");
        }
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
        IDocument doc = EditorUtilities.getDocument(editor);
        if (doc != null) {
            return doc.get();
        }
        return null;
    }
}
