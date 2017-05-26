/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.editor;

import org.jtool.changerecorder.operation.FileOperation;
import org.jtool.changerecorder.operation.IOperation;
import org.jtool.macrorecorder.recorder.MacroEvent;
import org.jtool.macrorecorder.util.EditorUtilities;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * Provides a Java-specific text editor on which editing operations are recorded.
 */
@SuppressWarnings("restriction")
public class JCompilationUnitEditor extends CompilationUnitEditor {
    
    /**
     * A manager that manages the operation history.
     */
    private HistoryManager historyManager = null;
    
    /**
     * Creates an instance of this editor.
     */
    public JCompilationUnitEditor() {
        super();
    }
    
    /**
     * Records the file open operation when the editor is instantiated.
     * @param site the editor site
     * @param input the editor input
     * @exception PartInitException if this editor was not initialized successfully
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        
        if (historyManager == null) {
            historyManager = HistoryManager.getInstance();
            historyManager.start(this);
            
            historyManager.recordFileOpenOperation(getInputFile(), getSourceCode());
        }
    }
    
    /**
     * Records the file activation operation when the editor is activated.
     */
    @Override
    public void setFocus() {
        super.setFocus();
        
        if (historyManager != null) {
            IOperation op = historyManager.getLastOperation();
            if (op != null && op.getOperationType() == IOperation.Type.FILE) {
                FileOperation fop = (FileOperation)op;
                if (fop.getActionType() == FileOperation.Type.ACT && fop.getFilePath().compareTo(getInputFilePath()) == 0) {
                    return;
                }
            }
            
            historyManager.recordFileOperation(getInputFile(), getSourceCode(), FileOperation.Type.ACT, false);
        }
    }
    
    /**
     * Records the file save operation and stores the operation history into the output file when the contents of the editor is saved.
     * @param progressMonitor the progress monitor for communicating result state, or <code>null</code> if not needed
     */
    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        
        if (historyManager != null) {
            historyManager.recordFileOperation(getInputFile(), getSourceCode(), FileOperation.Type.SAVE, true);
            historyManager.writeHistory(getInputFile());
        }
    }
    
    /**
     * Stores the operation history into the output file when the setting of the editor input is changed.
     * @param input the changed editor input
     */
    @Override
    protected void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
    }
    
    /**
     * Records the file close operation and stores the operation history into the output file when the editor is closed.
     */
    @Override
    public void dispose() {
        if (historyManager != null) {
            historyManager.stop(this);
            
            historyManager.recordFileCloseOperation(getInputFile(), getSourceCode());
            historyManager.writeHistory(getInputFile());
            historyManager = null;
        }
        
        super.dispose();
    }
    
    /**
     * Returns the path of a file existing on this editor. 
     * @return the file path, which is relative to the path of the path of the workspace
     */
    public String getInputFilePath() {
        return EditorUtilities.getInputFilePath(this);
    }
    
    /**
     * Returns a file existing on this editor.
     * @return the <code>IFile</code> instance corresponding to the file, or <code>null</code> if none
     */
    public IFile getInputFile() {
        return EditorUtilities.getInputFile(this);
    }
    
    /**
     * Obtains a document related to this editor. 
     * @return the related document, or <code>null</code> if none
     */
    public IDocument getDocument() {
        return EditorUtilities.getDocument(this);
    }
    
    /**
     * Obtains the contents of source code related to this editor. 
     * @return the contents of the source code, or <code>null</code> if the source code is not valid
     */
    public String getSourceCode() {
        return EditorUtilities.getSourceCode(this);
    }
    
    /**
     * Receives a macro event when a new macro is added.
     * @param evt the macro event
     */
    public void macroAdded(MacroEvent evt) {
        /*
        Macro macro = evt.getMacro();
        System.out.println(macro.toString());
        if (macro instanceof CompoundMacro) {
            CompoundMacro cmacro = (CompoundMacro)macro;
            for (Macro m : cmacro.getMacros()) {
                System.out.println(" " + m.toString());
            }
        }
        */
    }
    
    /**
     * Receives a macro event when a document is changed.
     * @param evt the raw macro event
     */
    public void documentChanged(MacroEvent evt) {
        // System.out.println("!MACRO: " + evt.getMacro());
    }
}
