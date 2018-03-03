package sysu.web.bean;

import java.util.List;

import sysu.commconsistency.bean.CommentEntry;

public class CommentCode {
	
	private int commentID;
	private String className;
	private int startLine;
	private int endLine;
	private String code;
	private String consistency;
	
	public CommentCode(CommentEntry comment) {
		commentID = comment.getCommentID();
		className = comment.getClassName();
		startLine = comment.getNew_scope_startLine();
		endLine = comment.getNew_scope_endLine();
		
		List<String> newCodeList = comment.getNewCode();
		List<String> oldCommentList = comment.getOldComment();
		
		StringBuilder sb = new StringBuilder();
		for(String str:oldCommentList) {
			sb.append(str).append("\r\n");
		}
		for(String str:newCodeList) {
			sb.append(str).append("\r\n");
		}
		
		code = sb.toString().replaceAll("<", "&lt;");
		
		consistency = comment.getIsChangeProbability()>=0.5?"不一致":"一致";
	}
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getStartLine() {
		return startLine;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getConsistency() {
		return consistency;
	}
	public void setConsistency(String consistency) {
		this.consistency = consistency;
	}
	
	

}
