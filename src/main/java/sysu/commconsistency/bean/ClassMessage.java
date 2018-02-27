package sysu.commconsistency.bean;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="class_message")
public class ClassMessage {
	@Id
	private String id;
	
	@Field("version_id")
	private int versionID;

	@Field("class_id")
	private int classID;
	
	@Field("class_name")
	private String className;
	
	private String type;
	
	@Field("old_code")
	private List<Line> oldCode;  
	
	@Field("new_code")
	private List<Line> newCode;
	
	@Field("new_tokens")
	private List<Token> newTokenList;
	
	@Field("old_tokens")
	private List<Token> oldTokenList;
	
	@Field("diffs")
	private List<DiffType> diffList;
	
	@Field("new_comments")
	private List<CodeComment> newComment;
	
	@Field("old_comments")
	private List<CodeComment> oldComment;
	
	@Field("codes")
	private List<Line> codes;
	
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public List<Token> getNewTokenList() {
		return newTokenList;
	}
	public void setNewTokenList(List<Token> newTokenList) {
		this.newTokenList = newTokenList;
	}
	public List<Token> getOldTokenList() {
		return oldTokenList;
	}
	public void setOldTokenList(List<Token> oldTokenList) {
		this.oldTokenList = oldTokenList;
	}
	public List<DiffType> getDiffList() {
		return diffList;
	}
	public void setDiffList(List<DiffType> diffList) {
		this.diffList = diffList;
	}
	public List<CodeComment> getNewComment() {
		return newComment;
	}
	public void setNewComment(List<CodeComment> newComment) {
		this.newComment = newComment;
	}
	public List<CodeComment> getOldComment() {
		return oldComment;
	}
	public void setOldComment(List<CodeComment> oldComment) {
		this.oldComment = oldComment;
	}
	public List<Line> getNewCode() {
		return newCode;
	}
	public void setNewCode(List<Line> newCode) {
		this.newCode = newCode;
	}
	public List<Line> getOldCode() {
		return oldCode;
	}
	public void setOldCode(List<Line> oldCode) {
		this.oldCode = oldCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Line> getCodes() {
		return codes;
	}
	public void setCodes(List<Line> codes) {
		this.codes = codes;
	}
	
	
}