package sysu.coreclass.bean;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection="method")
public class MethodBean {
	
	@Id
	private String id;
	
	
	


	
	@Field("version_id")
	private int versionID;
	
	@Field("class_id")
	private int classID;
	
	@Field("class_name")
	private String className;
	
	@Field("method_id")
	private int methodID;
	
	@Field("method_name")
	private String methodName;
	
	@Field("return_type")
	private String returnType;
	
	@Field("parameters")
	private List<String> parameters;
	
	@Field("call_methods")
	private List<String> callMethodList;
	
	@Field("method_type")
	private String methodType;
	
	@Field("comment")
	private String comment;
	
	@Field("statements")
	private List<StatementBean> statementList;
	
	@Field("inner_count")
	private int innerCount=0;
	
	@Field("outter_count")
	private int outterCount=0;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public int getMethodID() {
		return methodID;
	}

	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}


	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public List<String> getCallMethodList() {
		return callMethodList;
	}

	public void setCallMethodList(List<String> callMethodList) {
		this.callMethodList = callMethodList;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<StatementBean> getStatementList() {
		return statementList;
	}

	public void setStatementList(List<StatementBean> statementList) {
		this.statementList = statementList;
	}

	public int getInnerCount() {
		return innerCount;
	}

	public void setInnerCount(int innerCount) {
		this.innerCount = innerCount;
	}

	public int getOutterCount() {
		return outterCount;
	}

	public void setOutterCount(int outterCount) {
		this.outterCount = outterCount;
	}

	

}
