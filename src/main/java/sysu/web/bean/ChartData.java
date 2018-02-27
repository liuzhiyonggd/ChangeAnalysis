package sysu.web.bean;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
	
	private List<Data> datasets = new ArrayList<Data>();
	private List<String> labels = new ArrayList<String>();
	public List<Data> getDatasets() {
		return datasets;
	}
	public void setDatasets(List<Data> dataset) {
		this.datasets = dataset;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	

}
