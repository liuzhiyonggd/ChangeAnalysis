package sysu.commconsistency.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class Line {
	@Field("line_number")
	private int lineNumber;
	
	@Field("line")
	private String line;
	
	@Field("offset")
	private int offset;
	
	@Field("type")
	private String type;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
