/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.ContentAssistantFacade;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides utilities that obtain information on editors.
 * @author Katsuhisa Maruyama
 */
public class EditorUtilities {
    
    /**
     * Obtains the source viewer of an editor.
     * @param editor the editor
     * @return the source viewer of the editor
     */
    public static ISourceViewer getSourceViewer(IEditorPart editor) {
        if (editor == null) {
            return null;
        }
        
        ISourceViewer viewer = (ISourceViewer)editor.getAdapter(ITextOperationTarget.class);
        return viewer;
    }
    
    /**
     * Obtains the styled text of an editor.
     * @param editor the editor
     * @return the styled text of the editor
     */
    public static StyledText getStyledText(IEditorPart editor) {
        ISourceViewer viewer = getSourceViewer(editor);
        if (viewer != null) {
            return viewer.getTextWidget();
        }
        return null;
    }
    
    /**
     * Obtains the content assistant facade of an editor.
     * @param editor the editor
     * @return the content assistant facade of the editor
     */
    public static ContentAssistantFacade getContentAssistantFacade(IEditorPart editor) {
        ISourceViewer viewer = getSourceViewer(editor);
        if (viewer != null && viewer instanceof SourceViewer) {
            return ((SourceViewer)viewer).getContentAssistantFacade();
        }
        return null;
    }
    
    /**
     * Obtains a file existing on an editor.
     * @param editor the editor
     * @return the file existing on the editor, or <code>null</code> if none
     */
    public static IFile getInputFile(IEditorPart editor) {
        IEditorInput input = editor.getEditorInput();
        if (input instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput)input).getFile();
            return file;
        }
        return null;
    }
    
    /**
     * Obtains the path of a file existing on an editor.
     * @param editor the editor
     * @return the path of the file, which is relative to the path of the workspace
     */
    public static String getInputFilePath(IEditorPart editor) {
        IFile file = getInputFile(editor);
        return getInputFilePath(file);
    }
    
    /**
     * Obtains the path of a file.
     * @param file the file
     * @return the path of the file, which is relative to the path of the workspace
     */
    public static String getInputFilePath(IFile file) {
        if (file != null) {
            return file.getFullPath().toString();
        }
        return "";
    }
    
    /**
     * Obtains the document of a file.
     * @param file the file
     * @return the document of the file, or <code>null</code> if none
     */
    public static IDocument getDocument(IFile file) {
        if (file == null) {
            return null;
        }
        
        try {
            TextFileDocumentProvider provider = new TextFileDocumentProvider();
            provider.connect(file);
            IDocument doc = provider.getDocument(file);
            provider.disconnect(file);
            return doc;
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Obtains the document of a file existing on an editor.
     * @param editor the editor
     * @return the document of the file, or <code>null</code> if none
     */
    public static IDocument getDocument(IEditorPart editor) {
        IFile file = getInputFile(editor);
        if (file != null) {
            return getDocument(file);
        }
        return null;
    }
    
    /**
     * Obtains the contents of source code appearing in an editor.
     * @param editor the editor
     * @return the contents of the source code, or <code>null</code> if the source code is not valid
     */
    public static String getSourceCode(IEditorPart editor) {
        IDocument doc = getDocument(editor);
        if (doc != null) {
            return doc.get();
        }
        return null;
    }
    
    /**
     * Obtains the contents of source code appearing in an editor.
     * @param file the file
     * @return the contents of the source code, or <code>null</code> if the source code is not valid
     */
    public static String getSourceCode(IFile file) {
        IDocument doc = getDocument(file);
        if (doc != null) {
            return doc.get();
        }
        return null;
    }
    
    /**
     * Obtains an editor that may edits the contents of a file.
     * @param file the file
     * @return the editor of the file, or <code>null</code> if none
     */
    public static IEditorPart getEditor(IFile file) {
        IEditorInput input = new FileEditorInput(file);
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        
        for (IWorkbenchWindow window : windows) {
            IWorkbenchPage[] pages = window.getPages();
            
            for (IWorkbenchPage page : pages) {
                IEditorPart part = page.findEditor(input);
                return part;
            }
        }
        return null;
    }
    
    /**
     * Obtains all editors that are currently opened.
     * @return the collection of the opened editors
     */
    public static List<IEditorPart> getEditors() {
        List<IEditorPart> editors = new ArrayList<IEditorPart>();
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        
        for (IWorkbenchWindow window : windows) {
            IWorkbenchPage[] pages = window.getPages();
            
            for (IWorkbenchPage page : pages) {
                IEditorReference[] refs = page.getEditorReferences();
                
                for (IEditorReference ref : refs) {
                    IEditorPart part = ref.getEditor(false);
                    if (part != null) {
                        editors.add(part);
                    }
                }
            }
        }
        return editors;
    }
    
    /**
     * Obtains an editor that is currently active.
     * @return the active editor, or <code>null</code> if none
     */
    public static IEditorPart getActiveEditor() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            IEditorPart part = window.getActivePage().getActiveEditor();
            return part;
        }
        return null;
    }
    
    /**
     * Obtains the path of a active file existing on an editor.
     * @return the path of the file, which is relative to the path of the workspace
     */
    public static String getActiveInputFilePath() {
        IEditorPart editor = getActiveEditor();
        if (editor != null) {
            return getInputFilePath(editor);
        }
        return null;
    }
}
