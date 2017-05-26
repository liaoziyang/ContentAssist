package jp.ac.ritsumei.cs.fse.contentassist;

import java.util.ArrayList;
import java.util.List;

import jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser.ConsoleOperationListener2;
import jp.ac.ritsumei.cs.fse.contentassist.entity.ApplyOperation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jtool.changerecorder.editor.HistoryManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "Contentassist"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public static List<ApplyOperation> applyoperationlist = new ArrayList<ApplyOperation>();
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		HistoryManager hm = HistoryManager.getInstance();
		hm.addOperationEventListener(new ConsoleOperationListener2());
		System.out.println(PLUGIN_ID + " activated.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public void earlyStartup() {
		// TODO Auto-generated method stub
	
	}

}
