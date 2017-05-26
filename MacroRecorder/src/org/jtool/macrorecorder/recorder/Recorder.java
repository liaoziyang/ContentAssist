/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.recorder;

import org.jtool.macrorecorder.Activator;
import org.jtool.macrorecorder.internal.recorder.DocMacroRecorder;
import org.jtool.macrorecorder.internal.recorder.DocMacroRecorderOnEdit;
import org.jtool.macrorecorder.internal.recorder.DocMacroRecorderOffEdit;
import org.jtool.macrorecorder.internal.recorder.MenuMacroRecorder;
import org.jtool.macrorecorder.macro.Macro;
import org.jtool.macrorecorder.util.EditorUtilities;
import org.jtool.macrorecorder.util.WorkspaceUtilities;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.operation.IRunnableWithProgress;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Records operations and menu actions performed on Eclipse.
 * @author Katsuhisa Maruyama
 */
public class Recorder {
    
    /**
     * The single instance of this recorder.
     */
    private static Recorder instance = new Recorder();
    
    /**
     * The collection of recorders that record document macros.
     */
    private static Map<String, DocMacroRecorder> docRecorders = new HashMap<String, DocMacroRecorder>();
    
    /**
     * A recorder that records menu actions.
     */
    private MenuMacroRecorder menuRecorder;
    
    /**
     * A compressor that compresses macros.
     */
    private MacroCompressor compressor;
    
    /**
     * The collection of listeners that receives macro events.
     */
    private List<MacroListener> macroListeners = new ArrayList<MacroListener>();
    
    /**
     * Creates an object that records macros.
     */
    private Recorder() {
        this.compressor = new MacroCompressor();
    }
    
    /**
     * Returns the single instance of this recorder.
     * @return the single instance
     */
    public static Recorder getInstance() {
        return instance;
    }
    
    /**
     * Sets a compressor that compresses macros.
     * @param compressor the compressor
     */
    public void setMacroCompressor(MacroCompressor compressor) {
        if (compressor != null) {
            this.compressor = compressor;
        }
    }
    
    /**
     * Starts the recording of document macros performed on an editor.
     */
    public synchronized void start() {
        menuRecorder = MenuMacroRecorder.getInstance();
        menuRecorder.setRecorder(this);
        menuRecorder.start();
        
        startDocRecording(this);
    }
    
    /**
     * Starts the recording of document macros.
     * @param recorder the recorder
     */
    private void startDocRecording(final Recorder recorder) {
        Display.getDefault().asyncExec(new Runnable() {
            
            /**
             * Displays the progress monitor.
             */
            @Override
            public void run() {
                try {
                    IWorkbenchWindow window = Activator.getWorkbenchWindow();
                    // window.run(true, true, new IRunnableWithProgress() {
                    ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
                    dialog.run(true, true, new IRunnableWithProgress() {
                        
                        /**
                         * Attaches a recorder to each of collected source files.
                         * @param monitor the progress monitor to use to display progress
                         * @exception InvocationTargetException if the run method must propagate a checked exception
                         * @exception InterruptedException if the operation detects a request to cancel
                         */
                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            Collection<ICompilationUnit> cus = WorkspaceUtilities.collectAllCompilationUnits();
                            monitor.beginTask("Attaching a recorder ... ", cus.size());
                            
                            int num = 1;
                            for (ICompilationUnit cu : cus) {
                                IFile file = (IFile)cu.getResource();
                                
                                if (monitor.isCanceled()) {
                                    monitor.done();
                                    throw new InterruptedException();
                                }
                                
                                monitor.subTask(String.valueOf(num) + "/" + cus.size() + " " + file.getFullPath().toString());
                                
                                startDocRecording(file, recorder);
                                
                                num++;
                                monitor.worked(1);
                                // Thread.sleep(1000); // for testing
                            }
                            
                            monitor.done();
                        }
                    });
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    stop();
                }
            }
        });
    }
    
    private void startDocRecording(IFile file, Recorder recorder) {
        String path = file.getFullPath().toString();
        DocMacroRecorder docRecorder = getDocRecorder(path);
        if (docRecorder == null) {
            docRecorder = new DocMacroRecorderOffEdit(file, recorder);
            docRecorders.put(path, docRecorder);
            docRecorder.start();
        }
    }
    
    /**
     * Starts the recording of document macros performed on an editor.
     * @param editor the editor
     */
    public synchronized void start(IEditorPart editor) {
        String path = EditorUtilities.getInputFilePath(editor);
        DocMacroRecorder docRecorder = getDocRecorder(path);
        if (docRecorder == null) {
            docRecorder = new DocMacroRecorderOnEdit(editor, this, compressor);
            docRecorders.put(path, docRecorder);
            docRecorder.start();
            
        } else {
            docRecorder.stop();
            removeDocRecorder(path);
            
            docRecorder = new DocMacroRecorderOnEdit(editor, this, compressor);
            docRecorders.put(path, docRecorder);
            docRecorder.start();
        }
    }
    
    /**
     * Stops the recording of menu and document macros.
     */
    public synchronized void stop() {
        if (menuRecorder != null) {
            menuRecorder.stop();
        }
        
        for (DocMacroRecorder docRrecorder : getDocRecorders()) {
            docRrecorder.stop();
        }
        docRecorders.clear();
    }
    
    /**
     * Stops the recording of menu and document macros performed on an editor.
     * @param editor the editor
     */
    public synchronized void stop(IEditorPart editor) {
        String path = EditorUtilities.getInputFilePath(editor);
        DocMacroRecorder docRecorder = getDocRecorder(path);
        if (docRecorder != null) {
            docRecorder.stop();
            removeDocRecorder(path);
            
            IFile file = EditorUtilities.getInputFile(editor);
            docRecorder = new DocMacroRecorderOffEdit(file, this);
            docRecorders.put(path, docRecorder);
            docRecorder.start();
        }
    }
    
    /**
     * Adds a listener that receives a macro event.
     * @param listener the event listener to be added
     */
    public void addMacroListener(MacroListener listener) {
        macroListeners.add(listener);
    }
    
    /**
     * Removes a listener that receives a macro event.
     * @param listener the event listener to be removed
     */
    public void removeMacroListener(MacroListener listener) {
        macroListeners.remove(listener);
    }
    
    /**
     * Sends a macro event to all the listeners.
     * @param macro the macro sent to the listeners
     */
    public void notifyMacro(Macro macro) {
        MacroEvent evt = new MacroEvent(MacroEvent.GENERIC_MACRO, macro);
        for (MacroListener listener : macroListeners) {
            listener.macroAdded(evt);
        }
    }
    
    /**
     * Sends a macro event to all the listeners.
     * @param macro the macro sent to the listeners
     */
    public void notifyRawMacro(Macro macro) {
        MacroEvent evt = new MacroEvent(MacroEvent.RAW_MACRO, macro);
        for (MacroListener listener : macroListeners) {
            listener.documentChanged(evt);
        }
    }
    
    /**
     * Dumps the remaining macro of a file.
     * @param path the path of the file
     */
    public void dumpRemainingMacro(String path) {
        DocMacroRecorder docRecorder = getDocRecorder(path);
        if (docRecorder != null) {
            docRecorder.dumpLastDocumentMacro();
        }
    }
    
    /**
     * Dumps the remaining macro stored in all files.
     */
    public void dumpRemainingMacro() {
        for (DocMacroRecorder docRrecorder : getDocRecorders()) {
            docRrecorder.dumpLastDocumentMacro();
        }
    }
    
    /**
     * Break the current macro stored in a file.
     * @param path the path of the file
     */
    public void breakMacro(String path) {
        DocMacroRecorder docRecorder = getDocRecorder(path);
        if (docRecorder != null) {
            docRecorder.breakMacro();
        }
        
        if (menuRecorder != null) {
            menuRecorder.breakMacro();
        }
    }
    
    /**
     * Break the current macro stored in all files.
     */
    public void breakMacro() {
        for (DocMacroRecorder docRrecorder : getDocRecorders()) {
            docRrecorder.breakMacro();
        }
        
        if (menuRecorder != null) {
            menuRecorder.breakMacro();
        }
    }
    
    /**
     * Returns a recorder that records document macros related to a file.
     * @param path the path of the file
     * @return the recorder, or <code>null</code> if none
     */
    public static DocMacroRecorder getDocRecorder(String path) {
        return docRecorders.get(path);
    }
    
    /**
     * Removes a recorder that records document macros related to a file.
     * @param path the path of the file
     */
    public static void removeDocRecorder(String path) {
        docRecorders.remove(path);
    }
    
    /**
     * Returns all the recorders that record document macros.
     * @return the collection of the recorders
     */
    public static Collection<DocMacroRecorder> getDocRecorders() {
        return docRecorders.values();
    }
}
