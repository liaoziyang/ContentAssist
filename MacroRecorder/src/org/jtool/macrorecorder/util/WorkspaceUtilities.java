/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.util;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides utilities that obtain information on the workspace.
 * @author Katsuhisa Maruyama
 */
public class WorkspaceUtilities {
    
    /**
     * Obtains the workspace.
     * @return the workspace information
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }
    
    /**
     * Obtains the encoding to use when reading text files in the workspace.
     * @return the encoding
     */
    public static String getEncoding() {
        return ResourcesPlugin.getEncoding();
    }
    
    /**
     * Collects all compilation units within the project.
     * @return the collection of the compilation units
     */
    public static List<ICompilationUnit> collectAllCompilationUnits() {
        List<ICompilationUnit> units = new ArrayList<ICompilationUnit>();
        
        try {
            IProject[] projects =  getWorkspace().getRoot().getProjects();
            for (int i = 0; i < projects.length; i++) {
                IJavaProject project = (IJavaProject)JavaCore.create((IProject)projects[i]);
                
                IPackageFragment[] packages = project.getPackageFragments();
                for (int j = 0; j < packages.length; j++) {
                    
                    ICompilationUnit[] cus = packages[j].getCompilationUnits();
                    for (int k = 0; k < cus.length; k++) {
                        IResource res = cus[k].getResource();
                        if (res.getType() == IResource.FILE) {
                            String name = cus[k].getPath().toString();
                            if (name.endsWith(".java")) { 
                                units.add(cus[k]);
                            }
                        }
                    }
                }
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
        
        return units;
    }
}
