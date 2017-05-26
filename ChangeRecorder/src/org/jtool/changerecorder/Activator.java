/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder;

import org.jtool.changerecorder.editor.HistoryManager;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import java.io.File;

/**
 * The activator class that manages plug-in information.
 * @author Katsuhisa Maruyama
 */
public class Activator extends AbstractUIPlugin implements IStartup {
    
    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "ChangeRecorder";
    
    /**
     * The plug-in instance.
     */
    private static Activator plugin;
    
    /**
     * The top directory that stores history files.
     */
    public static String DEFAULT_HISTORY_TOPDIR = File.separator + "#history";
    
    /**
     * Creates a plug-in instance.
     */
    public Activator() {
    	
    }
    
    /**
     * Performs actions when the plug-in is activated.
     * @param context the bundle context for this plug-in
     * @throws Exception if this plug-in did not start up properly
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        
        HistoryManager.getInstance().start();
        
        System.out.println(PLUGIN_ID + " activated.");
    }
    
    /**
     * Performs actions when when the plug-in is shut down.
     * @param context the bundle context for this plug-in
     * @throws Exception if this this plug-in fails to stop
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        HistoryManager.getInstance().stop();
        
        super.stop(context);
        plugin = null;
    }
    
    /**
     * Will be called in a separate thread after the workbench initializes.
     */
    @Override
    public void earlyStartup() {
    }
    
    /**
     * Returns the default plug-in instance.
     * @return the default plug-in instance
     */
    public static Activator getPlugin() {
        return plugin;
    }
    
    /**
     * Obtains the workspace.
     * @return the workspace information
     */
    public IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }
    
    /**
     * Returns the directory path of the plug-in's workspace, which contains operation history. 
     * @return the the directory into which the operation history is stored
     */
    public String getOperationHistoryDirPath() {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IPath workspaceDir = workspaceRoot.getLocation();
        return workspaceDir.append(Activator.DEFAULT_HISTORY_TOPDIR).toString();
    }
}
