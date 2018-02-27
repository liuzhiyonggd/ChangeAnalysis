package sysu.commconsistency.bean;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;
public class DiffType {
	
	private String type;
	
	@Field("new_start_line")
	private int newStartLine;
	
	@Field("new_end_line")
	private int newEndLine;
	
	@Field("old_start_line")
	private int oldStartLine;
	
	@Field("old_end_line")
	private int oldEndLine;
	
	@Field("new_hashs")
	private List<Long> newHashList = new ArrayList<Long>();
	
	@Field("old_hashs")
	private List<Long> oldHashList = new ArrayList<Long>();
	
	@Field("new_keywords")
	private List<String> newKeywordList = new ArrayList<String>();
	
	@Field("old_keywords")
	private List<String> oldKeywordList = new ArrayList<String>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public List<Long> getNewHashList() {
		return newHashList;
	}
	public void setNewHashList(List<Long> newHashList) {
		this.newHashList = newHashList;
	}
	public List<Long> getOldHashList() {
		return oldHashList;
	}
	public void setOldHashList(List<Long> oldHashList) {
		this.oldHashList = oldHashList;
	}
	public void addNewHash(long newHash){
		newHashList.add(newHash);
	}
	public void addOldHash(long oldHash){
		oldHashList.add(oldHash);
	}
	public List<String> getNewKeywordList() {
		return newKeywordList;
	}
	public void setNewKeywordList(List<String> newKeywordList) {
		this.newKeywordList = newKeywordList;
	}
	public List<String> getOldKeywordList() {
		return oldKeywordList;
	}
	public void setOldKeywordList(List<String> oldKeywordList) {
		this.oldKeywordList = oldKeywordList;
	}
	public void addNewKeyword(String keyword){
		newKeywordList.add(keyword);
	}
	public void addOldKeyword(String keyword){
		oldKeywordList.add(keyword);
	}
	

}
