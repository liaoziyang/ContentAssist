
package jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser;

import java.util.List;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

/**
 * @author liaoziyang
 * Manage the Dataanalyser's work
 */
public class DataManager {
	public ConveyDatatoView conveydatatoview = new ConveyDatatoView();
	
	public getProposalResult getproposalresult;

	
	public HistoryRecorder historyrecorder = new HistoryRecorder();
	
	
	ContentAssistInvocationContext context = null;
	IProgressMonitor monitor = null;
	
	public DataManager(){}
	public DataManager(ContentAssistInvocationContext context, IProgressMonitor monitor){
		this.context = context;
		this.monitor = monitor;
		getproposalresult = new getProposalResult(context,monitor);
	}
	public void start(){
		//historyrecorder.getHistoryfromChangerecoder();
	}
	/**
	 * @return The default result of java content assist
	 */
	public List<ICompletionProposal> JavaDefaultProposal(){
		return getproposalresult.getAllProposalResult();	
	}
	
	public List<IContextInformation> ContextInformation(){	
		return getproposalresult.getAllProposalResultInformation();
	}

	public List<String> MethodName(){
		return null;
		//return divideproposal.getMethodName(context, monitor);
	}
}
