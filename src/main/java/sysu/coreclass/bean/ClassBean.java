package sysu.coreclass.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import sysu.commconsistency.bean.Line;



@Document(collection="class")
public class ClassBean {
	
	@Id
	private String id;
	
	@Field("version_id")
	private int versionID;
	
	@Field("class_id")
	private int classID;
	
	@Field("user_name")
	private String userName;
	
	@Field("class_name")
	private String className;
	
	@Field("parent_name")
	private String parentName;
	
	@Field("interfaces")
	private List<String> interfaceList;
	
	@Field("class_type")
	private String classType;
	
	@Field("comment")
	private String comment;
	
	@Field("new_code_lines")
	private int newCodeLines;
	
	@Field("old_code_lines")
	private int oldCodeLines;
	
	@Field("new_method_num")
	private int newMethodNum;
	
	@Field("old_method_num")
	private int oldMethodNum;
	
	@Field("inner_count")
	private int innerCount=0;
	
	@Field("outter_count")
	private int outterCount=0;
	
	@Field("is_core")
	private boolean isCore=false;
	
	@Field("change_method_num")
	private int changeMethodNum;
	
	@Field("add_method_num")
	private int addMethodNum;
	
	@Field("delete_method_num")
	private int deleteMethodNum;
	
	@Field("add_statement_node_types")
	private List<Integer> addStatementNodeTypeList;
	
	@Field("change_statement_node_types")
	private List<Integer> changeStatementNodeTypeList;
	
	@Field("delete_statement_node_types")
	private List<Integer> deleteStatementNodeTypeList;
	
	@Field("use_classes")
	private List<String> useClassList=new ArrayList<String>();
	
	@Field("method_invoke")
	private List<MethodInvoke> methodInvokeList = new ArrayList<MethodInvoke>();
	
	@Field("class_index")
	private int classIndex;
	
	@Field("codes")
	private List<Line> codes;
	
	@Field("core_probability")
	private double coreProbability;
	
	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public List<MethodInvoke> getMethodInvokeList() {
		return methodInvokeList;
	}

	public void setMethodInvokeList(List<MethodInvoke> methodInvokeList) {
		this.methodInvokeList = methodInvokeList;
	}

	public int getChangeMethodNum() {
		return changeMethodNum;
	}

	public void setChangeMethodNum(int changeMethodNum) {
		this.changeMethodNum = changeMethodNum;
	}

	public int getAddMethodNum() {
		return addMethodNum;
	}

	public void setAddMethodNum(int addMethodNum) {
		this.addMethodNum = addMethodNum;
	}

	public int getDeleteMethodNum() {
		return deleteMethodNum;
	}

	public List<Integer> getAddStatementNodeTypeList() {
		return addStatementNodeTypeList;
	}

	public void setAddStatementNodeTypeList(List<Integer> addStatementNodeTypeList) {
		this.addStatementNodeTypeList = addStatementNodeTypeList;
	}

	public List<Integer> getChangeStatementNodeTypeList() {
		return changeStatementNodeTypeList;
	}

	public void setChangeStatementNodeTypeList(List<Integer> changeStatementNodeTypeList) {
		this.changeStatementNodeTypeList = changeStatementNodeTypeList;
	}

	public List<Integer> getDeleteStatementNodeTypeList() {
		return deleteStatementNodeTypeList;
	}

	public void setDeleteStatementNodeTypeList(List<Integer> deleteStatementNodeTypeList) {
		this.deleteStatementNodeTypeList = deleteStatementNodeTypeList;
	}

	public void setDeleteMethodNum(int deleteMethodNum) {
		this.deleteMethodNum = deleteMethodNum;
	}
	public List<String> getUseClassList() {
		return useClassList;
	}

	public void setUseClassList(List<String> useClassList) {
		this.useClassList = useClassList;
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

	public boolean isCore() {
		return isCore;
	}

	public void setCore(boolean isCore) {
		this.isCore = isCore;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCoreProbability() {
		return coreProbability;
	}

	public void setCoreProbability(double coreProbability) {
		this.coreProbability = coreProbability;
	}

	public int getNewCodeLines() {
		return newCodeLines;
	}

	public void setNewCodeLines(int newCodeLines) {
		this.newCodeLines = newCodeLines;
	}

	public int getOldCodeLines() {
		return oldCodeLines;
	}

	public void setOldCodeLines(int oldCodeLines) {
		this.oldCodeLines = oldCodeLines;
	}

	public int getNewMethodNum() {
		return newMethodNum;
	}

	public void setNewMethodNum(int newMethodNum) {
		this.newMethodNum = newMethodNum;
	}

	public int getOldMethodNum() {
		return oldMethodNum;
	}

	public void setOldMethodNum(int oldMethodNum) {
		this.oldMethodNum = oldMethodNum;
	}

	public List<Line> getCodes() {
		return codes;
	}

	public void setCodes(List<Line> codes) {
		this.codes = codes;
	}	
	

}