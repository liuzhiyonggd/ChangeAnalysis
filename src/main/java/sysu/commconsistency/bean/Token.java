package sysu.commconsistency.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class Token {
	
	@Field("token_name")
	private String tokenName;
	
	private String keyword;
	
	@Field("start_line")
	private int startLine;
	
	@Field("end_line")
	private int endLine;
	
	@Field("hash_number")
	private long hashNumber;
	
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
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
	public long getHashNumber() {
		return hashNumber;
	}
	public void setHashNumber(long hashNumber) {
		this.hashNumber = hashNumber;
	}
	
	

}
