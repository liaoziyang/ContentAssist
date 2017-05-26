package jp.ac.ritsumei.cs.fse.contentassist.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.ac.ritsumei.cs.fse.contentassist.Activator;
import jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser.ConsoleOperationListener2;
import jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser.ConveyDatatoView;
import jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser.DataManager;
import jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser.getProposalResult;
import jp.ac.ritsumei.cs.fse.contentassist.entity.ApplyOperation;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.text.java.AbstractJavaCompletionProposal;
import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;

public class JavaCompletionProposalComputer1 implements
		IJavaCompletionProposalComputer {
	public JavaCompletionProposalComputer1() {
		// TODO Auto-generated constructor stub
		
	}

	public void sessionStarted() {
		// TODO Auto-generated method stub
		
	}
	public static final Styler LIAOZIYANG = StyledString.createColorRegistryStyler(
			JFacePreferences.ACTIVE_HYPERLINK_COLOR, null);
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
	    List<ICompletionProposal> propList = new ArrayList<ICompletionProposal>();
	    List<ICompletionProposal> newpropList = new ArrayList<ICompletionProposal>();
	    List<IContextInformation> propList2 = new ArrayList<IContextInformation>();
	    ICompletionProposal first;
	    DataManager datamanger = new DataManager(context,monitor);
	    Activator.applyoperationlist.add(new ApplyOperation(ConsoleOperationListener2.ope.getStart(), ConsoleOperationListener2.ope.getAuthor(), ConsoleOperationListener2.ope.getFilePath(), propList));
	   // Activator.applyoperationlist.add(new ApplyOperation(ConsoleOperationListener2.ope.getStart(),ConsoleOperationListener2.ope.getAuthor(),ConsoleOperationListener2.ope.getFilePath(),datamanger.getCAproposal()));
	    List<String> list = new ArrayList();
		//datamanger.getCAproposal();
		datamanger.start();
	    CompletionProposal proposal;
	    propList = datamanger.JavaDefaultProposal();
	    propList2 = datamanger.ContextInformation();
	    ApplyOperation ao = new ApplyOperation(ConsoleOperationListener2.ope.getStart(), ConsoleOperationListener2.ope.getAuthor(), ConsoleOperationListener2.ope.getFilePath(), propList);
	    
	    System.out.println(ao.toString());
	    //AbstractJavaCompletionProposal l = (AbstractJavaCompletionProposal)propList.get(1);
//	    for(int i=0;i<propList.size();i++){
//	    	newpropList.add((AbstractJavaCompletionProposal)propList.get(i));
//	    	StyledString text = ((AbstractJavaCompletionProposal) newpropList.get(i)).getStyledDisplayString();
//			text.append(" -"+(200-i)+"%",this.LIAOZIYANG);
//			((AbstractJavaCompletionProposal) newpropList.get(i)).setStyledDisplayString(text);
//			((AbstractJavaCompletionProposal) newpropList.get(i)).setRelevance(200-i);;
//			System.out.println(propList.get(i).toString());
//			//String[] sourceStrArray = propList.get(i).toString().split("(");
//	    	//System.out.println(sourceStrArray[0]);
//	    	//System.out.println(propList.get(i).toString());
//	    	//((AbstractJavaCompletionProposal) newpropList.get(i)).setStyledDisplayString(text)
//	    	
//	    }
	 
	    
	    return newpropList;
	}

	public List computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub

		return null;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sessionEnded() {
		// TODO Auto-generated method stub
		
	}

}
