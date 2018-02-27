package sysu.coreclass.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class StatementBean {

	@Field("statement_id")
	private int statementID;
	
	@Field("statement_type")
	private String statementeType;
	
	@Field("statement")
	private String statement;
	
	@Field("nodeType")
	private int nodeType;
	public int getStatementID() {
		return statementID;
	}
	public void setStatementID(int statementID) {
		this.statementID = statementID;
	}
	public String getStatementeType() {
		return statementeType;
	}
	public void setStatementeType(String statementeType) {
		this.statementeType = statementeType;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	
	
	
}
