package sysu.web.bean;

import java.util.ArrayList;
import java.util.List;

public class FormTreeData {
	
	private List<TreeCell> groups = new ArrayList<TreeCell>();
	
	public List<TreeCell> getGroups() {
		return groups;
	}

	public void setGroups(List<TreeCell> groups) {
		this.groups = groups;
	}

	public void addTreeCell(TreeCell cell) {
		groups.add(cell);
	}

}
