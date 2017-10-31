package jp.ac.ritsumei.cs.fse.contentassist.entity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class ApplyOperation {
	private int offset;
	private int last_offset;
	private  List<Character> InputString = new ArrayList<Character>();
	private long applytime;
	public String username;
	public String path;
	private boolean isApplied;
	private List<ICompletionProposal> propList;
	public ApplyOperation(int offset,String username,String path,List<ICompletionProposal> propList){
		this.offset = offset;
		this.last_offset = offset;
		this.username = username;
		this.path = path;
		this.propList = propList;
	        InputString.add('.');
		
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public List<Character> getInputString() {
		return InputString;
	}
	
	public long getApplytime() {
		return applytime;
	}
	public void setApplytime(long applytime) {
		this.applytime = applytime;
	}
	public String getUsername() {
		return username;
	}

	public String getPath() {
		return path;
	}
	public boolean isApplied(){
		return isApplied;
	}
	public void update(int operation_offset,String delete_string,String insert_string){
		if (operation_offset <= offset){
			offset = offset + delete_string.length() - insert_string.length();
			last_offset = last_offset + delete_string.length() - insert_string.length();
		}
		else if(last_offset > operation_offset&&operation_offset > offset){
				last_offset = last_offset + insert_string.length() - delete_string.length();
				for(int i=0;i<=operation_offset - offset;i++){
				InputString.remove(i);
			}
				InputString.add(operation_offset-offset, insert_string.charAt(0));
		}
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplyOperation [offset=" + offset + ", last_offset="
				+ last_offset + ", InputString=" + InputString + ", applytime="
				+ applytime + ", username=" + username + ", path=" + path
				+ ", propList=" + propList + "]";
	}
	
	
}
