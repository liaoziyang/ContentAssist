/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Stores a resource change macro.
 * @author Katsuhisa Maruyama
 */
public class ResourceMacro extends Macro {
    
    /**
     * The target of this resource change.
     */
    private String target;
    
    /**
     * The path of a resource identical to the changed resource.
     */
    private String identicalPath;
    
    /**
     * The contents of source code after the resource change.
     */
    private String code;
    
    /**
     * The encoding of the changed source code.
     */
    private String encoding;
    
    /**
     * A flag that indicates if the changed resource is currently edited.
     */
    private boolean onEdit = false;
    
    /**
     * Creates an object storing information on an resource change macro.
     * @param time the time when the macro started
     * @param type the type of the change
     * @param path the path of the changed resource
     * @param elem the changed resource
     */
    public ResourceMacro(long time, String type, String path, IJavaElement elem) {
        super(time, time, type, path);
        this.target = getTarget(elem);
        this.identicalPath = getIdenticalPath(elem);
        this.code = getCode(elem);
        this.encoding = getEncoding(elem);
    }
    
    /**
     * Creates an object storing information on an resource change macro.
     * @param time the time when the macro started
     * @param type the type of the change
     * @param path the path of the changed resource
     * @param res the changed resource
     */
    public ResourceMacro(long time, String type, String path, IResource res) {
        this(time, type, path, JavaCore.create(res));
    }
    
    /**
     * Returns the target of this resource change.
     * @param elem the changed element
     * @return the target of the change, or <code>null</code> if the target is not either a project, package, or file
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * Returns the path of a resource moved/renamed from or to.
     * @return the path of the identical resource
     */
    public String getIdenticalPath() {
        return identicalPath;
    }
    
    /**
     * Returns source code after this resource change.
     * @return the contents of the source code, or an empty string if the changed resource is not a file
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Returns the encoding of the changed source code.
     * @return the encoding of the changed source code, or <code>null</code>
     */
    public String getEncoding() {
        return encoding;
    }
    
    /**
     * Sets the flag that indicates if the changed resource is currently edited.
     * @param bool <code>true</code> if the changed resource is currently edited, otherwise <code>false</code>
     */
    public void setOnEdit(boolean bool) {
        onEdit = bool;
    }
    
    /**
     * Tests if the changed resource is currently edited.
     * @return <code>true</code> if the changed resource is currently edited, otherwise <code>false</code>
     */
    public boolean getOnEdit() {
        return onEdit;
    }
    
    /**
     * Tests if the type of the change belongs to removal.
     * @return <code>true</code> if this macro represents the removal change, otherwise <code>false</code>
     */
    public boolean isRemoved() {
        return type.compareTo("Removed") == 0 ||
               type.compareTo("RenamedTo") == 0 ||
               type.compareTo("MovedTo") == 0;
    }
    
    /**
     * Tests if the type of the change belongs to addition.
     * @return <code>true</code> if this macro represents the addition change, otherwise <code>false</code>
     */
    public boolean isAdded() {
        return type.compareTo("Added") == 0 ||
               type.compareTo("RenamedFrom") == 0 ||
               type.compareTo("MovedFrom") == 0;
    }
    
    /**
     * Returns the target of this resource change.
     * @param elem the changed element
     * @return the target of the change, or an empty string if the target is not either a project, package, or file
     */
    private String getTarget(IJavaElement elem) {
        if (elem == null) {
            return "";
        }
        
        int type = elem.getElementType();
        if (type == IJavaElement.JAVA_PROJECT) {
            return "Project";
            
        } else if (type == IJavaElement.PACKAGE_DECLARATION) {
            return "Package";
            
        } else if (type == IJavaElement.COMPILATION_UNIT) {
            return "File";
        }
        
        return elem.getElementName() + "@" + elem.getElementType();
    }
    
    /**
     * Returns the path of a resource moved/renamed from or to.
     * @param elem the changed resource
     * @return the path of the identical resource
     */
    private String getIdenticalPath(IJavaElement elem) {
        return elem.getPath().toString();
    }
    
    /**
     * Obtains source code after this resource change.
     * @param elem the changed resource
     * @return the contents of the source code, or an empty string if the changed resource is not a file
     */
    private String getCode(IJavaElement elem) {
        if (elem instanceof ICompilationUnit) {
            ICompilationUnit cu = (ICompilationUnit)elem;
            
            try {
                return cu.getSource();
            } catch (JavaModelException e) {
            }
        }
        return "";
    }
    
    /**
     * Returns the encoding of the changed source code.
     * @param elem the changed resource
     * @return the encoding of the source code, or <code>null</code>
     */
    private String getEncoding(IJavaElement elem) {
        if (elem instanceof ICompilationUnit) {
            ICompilationUnit cu = (ICompilationUnit)elem;
            
            try {
                IFile file = (IFile)cu.getCorrespondingResource();
                return file.getCharset();
            } catch (CoreException e) {
            }
        }
        return null;
    }
    
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "RESO(" + getType() + ")= " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getTarget() + " FROM/TO " + getIdenticalPath();
    }
    
    /**
     * Obtains the name of the specified element.
     * @param elem the element
     * @param type the type of the element
     * @return the name string
     */
    public static String getName(IJavaElement elem) {
        int type = elem.getElementType();
        if (type == IJavaElement.JAVA_PROJECT) {
            return elem.getResource().getName();
            
        } else if (type == IJavaElement.PACKAGE_DECLARATION) {
            IPackageFragment jpackage = (IPackageFragment)elem;
            return jpackage.getElementName();
            
        } else if (type == IJavaElement.COMPILATION_UNIT) {
            return elem.getResource().getName();
        }
        
        return "";
    }
}
