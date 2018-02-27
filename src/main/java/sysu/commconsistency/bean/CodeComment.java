package sysu.commconsistency.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class CodeComment {
	
	private String type;
	
	@Field("start_line")
	private int startLine;
	
	@Field("end_line")
	private int endLine;
	
	@Field("scope_startline")
	private int scopeStartLine;
	
	@Field("scope_endline")
	private int scopeEndLine;
	
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getScopeStartLine() {
		return scopeStartLine;
	}
	public void setScopeStartLine(int scopeStartLine) {
		this.scopeStartLine = scopeStartLine;
	}
	public int getScopeEndLine() {
		return scopeEndLine;
	}
	public void setScopeEndLine(int scopeEndLine) {
		this.scopeEndLine = scopeEndLine;
	}
	

}
