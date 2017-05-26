package jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser;

import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jdt.core.CompletionContext;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner;
import org.eclipse.jdt.internal.ui.text.Symbols;
import org.eclipse.jdt.internal.ui.text.java.*;
/**
 * @author liaoziyang
 * Get the proposal result of the default java content assist
 */
public class getProposalResult extends JavaAllCompletionProposalComputer{
	/**
	 * @param context The instance of ContentAssistInvocationContext
	 * @param monitor The instance of IProgressMonitor
	 * @see JavaAllCompletionProposalComputer.java in org.eclipse.jdt.internal.ui.text.java
	 * @return The proposal result of the default java content assist
	 */
	List<ICompletionProposal> proposallist;
	ContentAssistInvocationContext context;
	IProgressMonitor monitor;
	//List<CAProposal> caproposallist;
	public getProposalResult(ContentAssistInvocationContext context, IProgressMonitor monitor){
		// TODO Auto-generated constructor stub
		this.context = context;
		this.monitor = monitor;
		this.proposallist = computeCompletionProposals(context, monitor);
		//AbstractJavaCompletionProposal l = (AbstractJavaCompletionProposal)proposallist.get(1);
		
	}

	public List<ICompletionProposal> getAllProposalResult(){
	
		return proposallist;
	}
	public List<IContextInformation> getAllProposalResultInformation(){
		return computeContextInformation(context, monitor);
	}
}
