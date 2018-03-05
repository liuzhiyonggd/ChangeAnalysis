package sysu.web.bean;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="version")
public class Version {
	
	@Field("version_id")
	private int versionID;
	
	@Field("username")
	private String username;
	
	@Field("upload_time")
	private long uploadTime;
	
	@Field("format_upload_time")
	private String formatUploadTime;
	
	@Field("class_num")
	private int classNum;

	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getClassNum() {
		return classNum;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public String getFormatUploadTime() {
		return formatUploadTime;
	}

	public void setFormatUploadTime(String formatUploadTime) {
		this.formatUploadTime = formatUploadTime;
	}
	
	
	
}
