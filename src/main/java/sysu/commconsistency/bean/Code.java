package sysu.commconsistency.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Code {
	
	private List<Line> newLines = new ArrayList<Line>();
	private List<Line> oldLines = new ArrayList<Line>();
	
	public Code(){}
	
	public Code(String newPath,String oldPath){
		File newFile = new File(newPath);
		File oldFile = new File(oldPath);
		
		if(newFile.exists()){
			try {
				List<String> lines = FileUtils.readLines(newFile,"UTF-8");
				for(int i=0,n=lines.size();i<n;i++){
					Line line = new Line();
					line.setLine(lines.get(i));
					line.setLineNumber(i+1);
					newLines.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(oldFile.exists()){
			try {
				List<String> lines = FileUtils.readLines(oldFile,"UTF-8");
				for(int i=0,n=lines.size();i<n;i++){
					Line line = new Line();
					line.setLine(lines.get(i));
					line.setLineNumber(i+1);
					oldLines.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Line> getNewLines() {
		return newLines;
	}
	public void setNewLines(List<Line> newLines) {
		this.newLines = newLines;
	}
	public List<Line> getOldLines() {
		return oldLines;
	}
	public void setOldLines(List<Line> oldLines) {
		this.oldLines = oldLines;
	}
	
	

}
