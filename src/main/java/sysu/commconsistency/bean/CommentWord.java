package sysu.commconsistency.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="commentword")
public class CommentWord {

	@Id
	private String id;
	
	@Field("comment_id")
	private int commentID;
	
	private String project;
	private String type;
	private boolean ischange;
	
	private List<String> newCommentWords = new ArrayList<String>();
	private List<String> oldCommentWords = new ArrayList<String>();
	private List<String> newCodeWords = new ArrayList<String>();
	private List<String> oldCodeWords = new ArrayList<String>();
	
	private double codeSimilarity;
	private double newCodeOldCommentSimilarity;
	private double oldCodeOldCommentSimilarity;
	
	private List<String> addWords = new ArrayList<String>();
	private List<String> deleteWords = new ArrayList<String>();
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isIschange() {
		return ischange;
	}
	public void setIschange(boolean ischange) {
		this.ischange = ischange;
	}
	public List<String> getNewCommentWords() {
		return newCommentWords;
	}
	public void setNewCommentWords(List<String> newCommentWords) {
		this.newCommentWords = newCommentWords;
	}
	public List<String> getOldCommentWords() {
		return oldCommentWords;
	}
	public void setOldCommentWords(List<String> oldCommentWords) {
		this.oldCommentWords = oldCommentWords;
	}
	public List<String> getNewCodeWords() {
		return newCodeWords;
	}
	public void setNewCodeWords(List<String> newCodeWords) {
		this.newCodeWords = newCodeWords;
	}
	public List<String> getOldCodeWords() {
		return oldCodeWords;
	}
	public void setOldCodeWords(List<String> oldCodeWords) {
		this.oldCodeWords = oldCodeWords;
	}
	public double getCodeSimilarity() {
		return codeSimilarity;
	}
	public void setCodeSimilarity(double codeSimilarity) {
		this.codeSimilarity = codeSimilarity;
	}
	public double getNewCodeOldCommentSimilarity() {
		return newCodeOldCommentSimilarity;
	}
	public void setNewCodeOldCommentSimilarity(double newCodeOldCommentSimilarity) {
		this.newCodeOldCommentSimilarity = newCodeOldCommentSimilarity;
	}
	public double getOldCodeOldCommentSimilarity() {
		return oldCodeOldCommentSimilarity;
	}
	public void setOldCodeOldCommentSimilarity(double oldCodeOldCommentSimilarity) {
		this.oldCodeOldCommentSimilarity = oldCodeOldCommentSimilarity;
	}
	public List<String> getAddWords() {
		return addWords;
	}
	public void setAddWords(List<String> addWords) {
		this.addWords = addWords;
	}
	public List<String> getDeleteWords() {
		return deleteWords;
	}
	public void setDeleteWords(List<String> deleteWords) {
		this.deleteWords = deleteWords;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
