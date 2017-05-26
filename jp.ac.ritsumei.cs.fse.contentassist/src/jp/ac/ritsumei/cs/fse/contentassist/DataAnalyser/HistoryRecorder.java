package jp.ac.ritsumei.cs.fse.contentassist.DataAnalyser;

public class HistoryRecorder {
	
	public String getInsertString(){
		return ConsoleOperationListener2.ope.getInsertedText();
	}
	
	public String getDeleteString(){
		return ConsoleOperationListener2.ope.getDeletedText();
	}
	
	public int getOffset(){
		return ConsoleOperationListener2.ope.getStart();
	}
	public long getTime(){
		return ConsoleOperationListener2.ope.getTime();
	}
	public long getTimes(){
		return ConsoleOperationListener2.ope.getSequenceNumber();
	}
	public String getAuthor(){
		return ConsoleOperationListener2.ope.getAuthor();
	}
}
