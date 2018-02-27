package sysu.web.bean;

import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private List<Double> data = new ArrayList<Double>();
	
	private String backgroundColor;
	private String BorderColor;
	private String label;
	public List<Double> getData() {
		return data;
	}
	public void setData(List<Double> data) {
		this.data = data;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getBorderColor() {
		return BorderColor;
	}
	public void setBorderColor(String borderColor) {
		BorderColor = borderColor;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
	

}
