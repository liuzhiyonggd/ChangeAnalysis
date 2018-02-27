package sysu.commconsistency.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection="method")
public class MethodBean {

	@Id
	private String id;
	@Field("project")
	private String project;
	@Field("commit_id")
	private String commitID;
	@Field("class_name")
	private String className;
	@Field("class_id")
	private int classID;
	@Field("new_method_name")
	private String newMethodName;
	@Field("old_method_name")
	private String oldMethodName;
	@Field("new_parameters")
	private List<String> newParameters;
	@Field("old_parameters")
	private List<String> oldParameters;
	@Field("new_return_type")
	private String newReturnType;
	@Field("old_return_type")
	private String oldReturnType;
	@Field("new_start_line")
	private int newStartLine;
	@Field("new_end_line")
	private int newEndLine;
	@Field("old_start_line")
	private int oldStartLine;
	@Field("old_end_line")
	private int oldEndLine;
	@Field("method_id")
	private int methodID;
	@Field("diffs")
	private List<DiffType> diffList = new ArrayList<DiffType>();
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getNewMethodName() {
		return newMethodName;
	}
	public void setNewMethodName(String newMethodName) {
		this.newMethodName = newMethodName;
	}
	public String getOldMethodName() {
		return oldMethodName;
	}
	public void setOldMethodName(String oldMethodName) {
		this.oldMethodName = oldMethodName;
	}
	
	public List<String> getNewParameters() {
		return newParameters;
	}
	public void setNewParameters(List<String> newParameters) {
		this.newParameters = newParameters;
	}
	public List<String> getOldParameters() {
		return oldParameters;
	}
	public void setOldParameters(List<String> oldParameters) {
		this.oldParameters = oldParameters;
	}
	public String getNewReturnType() {
		return newReturnType;
	}
	public void setNewReturnType(String newReturnType) {
		this.newReturnType = newReturnType;
	}
	public String getOldReturnType() {
		return oldReturnType;
	}
	public void setOldReturnType(String oldReturnType) {
		this.oldReturnType = oldReturnType;
	}
	public int getNewStartLine() {
		return newStartLine;
	}
	public void setNewStartLine(int newStartLine) {
		this.newStartLine = newStartLine;
	}
	public int getNewEndLine() {
		return newEndLine;
	}
	public void setNewEndLine(int newEndLine) {
		this.newEndLine = newEndLine;
	}
	public int getOldStartLine() {
		return oldStartLine;
	}
	public void setOldStartLine(int oldStartLine) {
		this.oldStartLine = oldStartLine;
	}
	public int getOldEndLine() {
		return oldEndLine;
	}
	public void setOldEndLine(int oldEndLine) {
		this.oldEndLine = oldEndLine;
	}
	public int getMethodID() {
		return methodID;
	}
	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}
	public List<DiffType> getDiffList() {
		return diffList;
	}
	public void setDiffList(List<DiffType> diffList) {
		this.diffList = diffList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	
	
	
}
