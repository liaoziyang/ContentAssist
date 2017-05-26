/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.editor;

import org.jtool.changerecorder.diff.DiffOperationGenerator;
import org.jtool.changerecorder.event.OperationEventListener;
import org.jtool.changerecorder.event.OperationEventSource;
import org.jtool.changerecorder.history.OperationHistory;
import org.jtool.changerecorder.operation.CompoundOperation;
import org.jtool.changerecorder.operation.CopyOperation;
import org.jtool.changerecorder.operation.FileOperation;
import org.jtool.changerecorder.operation.IOperation;
import org.jtool.changerecorder.operation.MenuOperation;
import org.jtool.changerecorder.operation.NormalOperation;
import org.jtool.changerecorder.operation.TextOperation;
import org.jtool.changerecorder.util.Time;
import org.jtool.macrorecorder.recorder.Recorder;
import org.jtool.macrorecorder.recorder.MacroEvent;
import org.jtool.macrorecorder.recorder.MacroListener;
import org.jtool.macrorecorder.macro.Macro;
import org.jtool.macrorecorder.macro.ExecutionMacro;
import org.jtool.macrorecorder.macro.CopyMacro;
import org.jtool.macrorecorder.macro.DocumentMacro;
import org.jtool.macrorecorder.macro.CompoundMacro;
import org.jtool.macrorecorder.macro.ResourceMacro;
import org.jtool.macrorecorder.util.WorkspaceUtilities;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;

import java.util.List;
import java.util.ArrayList;

/**
 * A manager that manages the operation history.
 * @author Katsuhisa Maruyama
 */
public class HistoryManager extends OperationEventSource implements MacroListener {
    
    /**
     * The single instance of this history manager.
     */
    private static HistoryManager instance = new HistoryManager();
    
    /**
     * The operation history.
     */
    private OperationHistory history = new OperationHistory();
    
    /**
     * A recorder that records macros.
     */
    private Recorder recorder = null;
    
    /**
     * The new operation that exists before the current operation.
     */
    private FileOperation newOperation = null;
    
    /**
     * The close operation that exists before the current operation.
     */
    private FileOperation closeOperation = null;
    
    /**
     * A listener that displays operation on the console.
     */
    private OperationEventListener consoleOperationListener = new ConsoleOperationListener();
    
    /**
     * Creates a manager that records operations performed on an editor.
     */
    public HistoryManager() {
        super();
        recorder = Recorder.getInstance();
    }
    
    /**
     * Returns the single instance that manages the operation history.
     * @return the history manager
     */
    public static HistoryManager getInstance() {
        return instance;
    }
    
    /**
     * Starts recording of operations.
     */
    public void start() {
        recorder.addMacroListener(this);
        addOperationEventListener(consoleOperationListener);
    }
    
    /**
     * Starts recording of operations on an editor.
     * @param editor the editor
     */
    public void start(IEditorPart editor) {
        if (recorder != null) {
            recorder.start(editor);
        }
    }
    
    /**
     * Stops recording of operations.
     */
    public void stop() {
        if (recorder != null) {
            recorder.removeMacroListener(this);
            removeOperationEventListener(consoleOperationListener);
        }
    }
    
    /**
     * Stops recording of operations.
     * @param editor the editor
     */
    public void stop(IEditorPart editor) {
        if (recorder != null) {
            recorder.stop(editor);
        }
    }
    
    /**
     * Receives a macro event when a new macro is added.
     * @param evt the macro event
     */
    @Override
    public void macroAdded(MacroEvent evt) {
        Macro macro = evt.getMacro();
        // System.out.println(macro.toString());
        
        IOperation op = null;
        if (macro instanceof DocumentMacro) {
            op = createOperation((DocumentMacro)macro);
            
        } else if (macro instanceof ExecutionMacro) {
            op = createOperation((ExecutionMacro)macro);
            
        } else if (macro instanceof CopyMacro) {
            op = createOperation((CopyMacro)macro);
            
        } else if (macro instanceof CompoundMacro) {
            op = createOperation((CompoundMacro)macro);
            
        } else if (macro instanceof ResourceMacro) {
            createOperation((ResourceMacro)macro);
        }
        
        if (op != null) {
            storeOperation(op);
            
            newOperation = null;
            closeOperation = null;
            
        }
    }
    
    /**
     * Receives a macro event when a document is changed.
     * @param evt the raw macro event
     */
    @Override
    public void documentChanged(MacroEvent evt) {
        // System.out.println("!MACRO: " + evt.getMacro());
    }
    
    /**
     * Stores an operation into the operation history.
     * @param op the operation to be stored
     */
    private void storeOperation(IOperation op) {
        history.add(op);
        notify(op);
    }
    
    /**
     * Obtains the last operation from this operation history.
     * @return the last operation, or <code>null</code> if none
     */
    IOperation getLastOperation() {
        return history.getLastOperation();
    }
    
    /**
     * Records a file open operation.
     * @param file the file
     * @param code the contents of the source code when the operation was performed
     */
    void recordFileOpenOperation(IFile file, String code) {
        String path = file.getFullPath().toString();
        
        if (newOperation != null) {
            FileOperation op = new FileOperation(Time.getCurrentTime(), path, FileOperation.Type.OPEN, "");
            storeOperation(op);
            
            NormalOperation nop = new NormalOperation(Time.getCurrentTime(), 0, path, 0, code, "", NormalOperation.Type.EDIT);
            storeOperation(nop);
            
        } else {
            FileOperation op = new FileOperation(Time.getCurrentTime(), path, FileOperation.Type.OPEN, code);
            storeOperation(op);
        }
        
        newOperation = null;
        closeOperation = null;
    }
    
    /**
     * Records a file close operation.
     * @param file the file
     * @param code the contents of the source code when the operation was performed
     */
    void recordFileCloseOperation(IFile file, String code) {
        String path = file.getFullPath().toString();
        closeOperation = new FileOperation(Time.getCurrentTime(), path, FileOperation.Type.CLOSE, code);
        storeOperation(closeOperation);
    }
    
    /**
     * Records a file operation.
     * @param file the file
     * @param code the contents of the source code when the operation was performed
     * @param type the type of the operation
     * @param codeWrite <code>true</code> if source code will be written, otherwise <code>false</code>
     */
    void recordFileOperation(IFile file, String code, FileOperation.Type type, boolean codeWrite) {
        FileOperation op;
        if (codeWrite) {
            op = new FileOperation(Time.getCurrentTime(), file.getFullPath().toString(), type, code);
        } else {
            op = new FileOperation(Time.getCurrentTime(), file.getFullPath().toString(), type, null);
        }
        
        storeOperation(op);
    }
    
    /**
     * Creates a text operation from a macro.
     * @param macro the recorded macro
     * @return the created operation
     */
    private TextOperation createOperation(DocumentMacro macro) {
        NormalOperation.Type type = NormalOperation.Type.EDIT;
        if (macro.getType().compareTo("Cut") == 0) {
            type = NormalOperation.Type.CUT;
        } else if (macro.getType().compareTo("Paste") == 0) {
            type = NormalOperation.Type.PASTE;
        } else if (macro.getType().compareTo("Undo") == 0) {
            type = NormalOperation.Type.UNDO;
        } else if (macro.getType().compareTo("Redo") == 0) {
            type = NormalOperation.Type.REDO;
        }
        
        return new NormalOperation(Time.getCurrentTime(), macro.getPath(), 
                                   macro.getStart(), macro.getInsertedText(), macro.getDeletedText(), type);
    }
    
    /**
     * Creates a menu operation from a macro.
     * @param macro the recorded macro
     * @return the created operation
     */
    private IOperation createOperation(ExecutionMacro macro) {
        return new MenuOperation(macro.getStartTime(), macro.getPath(), macro.getCommandId());
    }
    
    /**
     * Creates a copy operation from a macro.
     * @param macro the recorded macro
     * @return the created operation
     */
    private IOperation createOperation(CopyMacro macro) {
        return new CopyOperation(Time.getCurrentTime(), macro.getPath(), macro.getStart(), macro.getCopiedText());
    }
    
    /**
     * Creates a compound operation from a macro.
     * @param macro the recorded macro
     * @return the created operation
     */
    private IOperation createOperation(CompoundMacro macro) {
        if (macro.getMacros().size() == 0) {
            return null;
        }
        
        List<IOperation> ops = new ArrayList<IOperation>();
        for (Macro m : macro.getMacros()) {
            if (m instanceof DocumentMacro) {
                ops.add(createOperation((DocumentMacro)m));
            }
        }
        return new CompoundOperation(macro.getStartTime(), ops, macro.getType());
    }
    
    /**
     * Creates a compound operation from a macro.
     * @param macro the recorded macro
     * @return the created operation
     */
    private void createOperation(ResourceMacro macro) {
        if (macro.getTarget().compareTo("File") == 0) {
            
            if (macro.isAdded()) {
                newOperation = new FileOperation(macro.getStartTime(), macro.getPath(), FileOperation.Type.NEW, "");
                storeOperation(newOperation);
                
            } else if (macro.isRemoved()) {
                
                if (closeOperation != null) {
                    CompoundOperation cop = createDiffOperation(closeOperation.getFilePath(), closeOperation.getCode(), "");
                    storeOperation(cop);
                }
                
                IOperation op = new FileOperation(Time.getCurrentTime(), macro.getPath(), FileOperation.Type.DELETE, macro.getCode());
                storeOperation(op);
                writeHistory(macro.getEncoding());
                
                closeOperation = null;
            }
        }
    }
    
    /**
     * Creates a compound operation that contains normal operations representing respective differences.
     * @param path the name of the file path on which this operation was performed
     * @param oldCode the old contents of the file
     * @param newCode the new contents of the file
     * @return the created compound operation
     */
    private CompoundOperation createDiffOperation(String path, String oldCode, String newCode) {
        long time = Time.getCurrentTime();
        List<NormalOperation> ops = DiffOperationGenerator.generate(time, path, oldCode, newCode);
        return new CompoundOperation(time, ops, "Diff");
    }
    
    /**
     * Writes the operation history related to a file.
     * @param file the file
     */
    void writeHistory(IFile file) {
        String encoding = null;
        try {
            if (file != null) {
                encoding = file.getCharset();
            }
        } catch (CoreException e) {
        }
        
        writeHistory(encoding);
    }
    
    /**
     * Writes the operation history related to a file.
     * @param encoding the encoding of the file
     */
    void writeHistory(String encoding) {
        if (!toBeWritten(history)) {
            return;
        }
        
        history.sort();
        
        if (encoding == null) {
            encoding = WorkspaceUtilities.getEncoding();
        }
        
        String dpath = OperationHistory.getOperationHistoryDirPath();
        String wpath = dpath + '/' + String.valueOf(Time.getCurrentTime()) + ".xml";
        
        history.write(wpath, encoding);
        // System.out.println(history.toString());
        
        history.clear();
    }
    
    /**
     * Tests if a given history will be written.
     * @param history the history to be checked
     * @return <code>true</code> the history should be written, otherwise <code>false</code>
     */
    private boolean toBeWritten(OperationHistory history) {
        if (history.size() == 0) {
            return false;
        }
        
        if (history.size() == 1) {
            return true;
        }
        
        IOperation op1 = history.getOperation(0);
        if (op1.getOperationType() != IOperation.Type.FILE) {
            return true;
        }
        
        FileOperation fop1 = (FileOperation)op1;
        if (fop1.getActionType() != FileOperation.Type.OPEN) {
            return true;
        }
        
        IOperation op2 = history.getOperation(history.size() - 1);
        if (op2.getOperationType() != IOperation.Type.FILE) {
            return true;
        }
        
        FileOperation fop2 = (FileOperation)op2;
        if (fop2.getActionType() != FileOperation.Type.CLOSE) {
            return true;
        }
        
        for (int idx = 1; idx < history.size() - 1; idx++) {
            IOperation op = history.getOperation(idx);
            if (op.isTextEditOperation()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Records a resource change operation.
     * @param path the path name of the file on which the operation was performed
     * @param code the contents of the source code when the operation was performed
     * @param type the type of the operation
     * @param codeWrite <code>true</code> if source code will be written, otherwise <code>false</code>
     */
    void recordResourceOperation(ResourceMacro macro) {
        // System.out.println(op.toString());
    }
}
