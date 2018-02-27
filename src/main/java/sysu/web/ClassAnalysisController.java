package sysu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sysu.commconsistency.bean.Line;
import sysu.coreclass.bean.ClassBean;
import sysu.coreclass.classify.CoreClassClassifier;
import sysu.coreclass.classify.DataGenerator;
import sysu.coreclass.web.service.ClassService;
import sysu.web.bean.ChartData;
import sysu.web.bean.ClassCode;
import sysu.web.bean.Data;
import sysu.web.bean.FormTreeData;
import sysu.web.bean.LinkData;
import sysu.web.bean.NodeData;
import sysu.web.bean.TreeCell;

@Controller
public class ClassAnalysisController {

	@Autowired
	private ClassService classService;

	private static CoreClassClassifier coreClassClassifier;

	static {
		coreClassClassifier = new CoreClassClassifier();
	}

	@RequestMapping(value = "/changestatistics/changestatistic", method = RequestMethod.GET)
	public String changeStatistic(Model model, HttpServletRequest request) throws IOException {

		int versionID = (Integer) request.getSession().getAttribute("versionID");
		List<ClassBean> classList = classService.findByVersionID(versionID);

		model.addAttribute("classInfoList", classList);

		List<String> labels = new ArrayList<String>();
		List<Double> addMethodNumList = new ArrayList<Double>();
		List<Double> changeMethodNumList = new ArrayList<Double>();
		List<Double> deleteMethodNumList = new ArrayList<Double>();

		List<Double> addStatementNumList = new ArrayList<Double>();
		List<Double> changeStatementNumList = new ArrayList<Double>();
		List<Double> deleteStatementNumList = new ArrayList<Double>();

		List<Double> innerCountList = new ArrayList<Double>();
		List<Double> outterCountList = new ArrayList<Double>();

		ChartData methodChartData = new ChartData();
		ChartData statementChartData = new ChartData();
		ChartData innerCountChartData = new ChartData();
		ChartData outterCountChartData = new ChartData();

		for (ClassBean clazz : classList) {
			labels.add("class_" + clazz.getClassID());

			addMethodNumList.add((double)clazz.getAddMethodNum());
			changeMethodNumList.add((double)clazz.getChangeMethodNum());
			deleteMethodNumList.add((double)clazz.getDeleteMethodNum());

			addStatementNumList.add((double)clazz.getAddStatementNodeTypeList().size());
			changeStatementNumList.add((double)clazz.getChangeStatementNodeTypeList().size());
			deleteStatementNumList.add((double)clazz.getDeleteStatementNodeTypeList().size());

			innerCountList.add((double)clazz.getInnerCount());
			outterCountList.add((double)clazz.getOutterCount());
		}

		methodChartData.setLabels(labels);
		List<Data> methodDatasets = new ArrayList<Data>();
		Data addMethodData = new Data();
		
		addMethodData.setData(addMethodNumList);
		addMethodData.setLabel("新增的方法个数");
		addMethodData.setBackgroundColor("rgba(72,118,255,0.8)");
		addMethodData.setBorderColor("rgba(72,118,255,1)");
		methodDatasets.add(addMethodData);
		Data changeMethodData = new Data();
		changeMethodData.setData(changeMethodNumList);
		changeMethodData.setLabel("修改的方法个数");

		changeMethodData.setBackgroundColor("rgba(121,205,205,0.8)");
		changeMethodData.setBorderColor("rgba(121,205,205,1)");
		methodDatasets.add(changeMethodData);

		Data deleteMethodData = new Data();
		deleteMethodData.setData(deleteMethodNumList);
		deleteMethodData.setLabel("删除的方法个数");
		deleteMethodData.setBackgroundColor("rgba(255,114,86,0.8)");
		deleteMethodData.setBorderColor("rgba(255,114,86,1)");
		methodDatasets.add(deleteMethodData);

		methodChartData.setDatasets(methodDatasets);
		JSONObject jsonMethodChartData = JSONObject.fromObject(methodChartData);
		model.addAttribute("methodChartData", jsonMethodChartData);

		statementChartData.setLabels(labels);
		List<Data> statementDatasets = new ArrayList<Data>();
		Data addStatementData = new Data();
		addStatementData.setData(addStatementNumList);
		addStatementData.setLabel("新增的语句个数");
		addStatementData.setBackgroundColor("rgba(72,118,255,0.8)");
		addStatementData.setBorderColor("rgba(72,118,255,1)");
		statementDatasets.add(addStatementData);

		Data changeStatementData = new Data();
		changeStatementData.setData(changeStatementNumList);
		changeStatementData.setLabel("修改的语句个数");
		changeStatementData.setBackgroundColor("rgba(121,205,205,0.8)");
		changeStatementData.setBorderColor("rgba(121,205,205,1)");
		statementDatasets.add(changeStatementData);

		Data deleteStatementData = new Data();
		deleteStatementData.setData(deleteStatementNumList);
		deleteStatementData.setLabel("删除的语句个数");
		deleteStatementData.setBackgroundColor("rgba(255,114,86,0.8)");
		deleteStatementData.setBorderColor("rgba(255,114,86,1)");
		statementDatasets.add(deleteStatementData);

		statementChartData.setDatasets(statementDatasets);
		JSONObject jsonStatementChartData = JSONObject.fromObject(statementChartData);
		model.addAttribute("statementChartData", jsonStatementChartData);

		innerCountChartData.setLabels(labels);
		List<Data> innerCountDatasets = new ArrayList<Data>();
		Data innerCountData = new Data();
		innerCountData.setData(innerCountList);
		innerCountData.setLabel("类的入度");
		innerCountData.setBackgroundColor("rgba(72,118,255,0.8)");
		innerCountData.setBorderColor("rgba(72,118,255,1)");
		innerCountDatasets.add(innerCountData);
		innerCountChartData.setDatasets(innerCountDatasets);
		JSONObject jsonInnerCountChartData = JSONObject.fromObject(innerCountChartData);
		model.addAttribute("innerCountChartData", jsonInnerCountChartData);

		outterCountChartData.setLabels(labels);
		List<Data> outterCountDatasets = new ArrayList<Data>();
		Data outterCountData = new Data();
		outterCountData.setData(outterCountList);
		outterCountData.setLabel("类的出度");
		outterCountData.setBackgroundColor("rgba(72,118,255,0.8)");
		outterCountData.setBorderColor("rgba(72,118,255,1)");
		outterCountDatasets.add(outterCountData);
		outterCountChartData.setDatasets(outterCountDatasets);
		JSONObject jsonOutterCountChartData = JSONObject.fromObject(outterCountChartData);
		model.addAttribute("outterCountChartData", jsonOutterCountChartData);

		return "changestatistics/changestatistic";

	}

	@RequestMapping(value = "/coreclass/coreclass", method = RequestMethod.GET)
	public String codeClass(Model model, HttpServletRequest request) {

		int versionID = (Integer) request.getSession().getAttribute("versionID");
		List<List<Integer>> vectors = DataGenerator.generate(versionID);
		List<ClassBean> classList = classService.findByVersionID(versionID);

		String[] colors = new String[] { "lightblue", "lightgreen", "pink", "orange", "grey", "violet" };
		List<NodeData> nodeList = new ArrayList<NodeData>();
		List<LinkData> linkList = new ArrayList<LinkData>();
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		for (int i = 0; i < classList.size(); i++) {
			classMap.put(classList.get(i).getClassName(), i);
		}
		for (int i = 0; i < classList.size(); i++) {
			NodeData nodeData = new NodeData();
			nodeData.setKey(i);
			nodeData.setText(classList.get(i).getClassName());
			nodeData.setColor(colors[i % colors.length]);
			nodeList.add(nodeData);
			List<String> useClasses = classList.get(i).getUseClassList();
			for (String str : useClasses) {
				LinkData linkData = new LinkData();
				linkData.setFrom(i);
				linkData.setTo(classMap.get(str));
				linkData.setColor("blue");
				linkList.add(linkData);
			}
		}
		JSONArray jsonNodeList = JSONArray.fromObject(nodeList);
		model.addAttribute("nodeList",jsonNodeList);
		JSONArray jsonLinkList = JSONArray.fromObject(linkList);
        model.addAttribute("linkList", jsonLinkList);
		
		List<Double> result = new ArrayList<Double>();
		try {
			result = coreClassClassifier.classify(vectors);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FormTreeData formTreeData = new FormTreeData();
		for(int i=0;i<classList.size();i++)
		{
			TreeCell cell = new TreeCell();
			cell.setLabel(classList.get(i).getClassName());
			cell.setWeight(result.get(i));
			formTreeData.addTreeCell(cell);
			classList.get(i).setCoreProbability(result.get(i));
			
		}
		JSONObject jsonFormTreeData = JSONObject.fromObject(formTreeData);
		model.addAttribute("formTreeData", jsonFormTreeData);
		
		Collections.sort(classList,new Comparator<ClassBean>() {
			@Override
			public int compare(ClassBean class1,ClassBean class2) {
				return class1.getCoreProbability()>class2.getCoreProbability()?-1:1;
			}
		});
		
		
		
		List<List<Integer>> highLightList = new ArrayList<List<Integer>>();
		
		List<ClassCode> codeList = new ArrayList<ClassCode>();
		for(ClassBean clazz:classList) {
		    StringBuilder sb = new StringBuilder();
		    
		    List<Integer> highLight = new ArrayList<Integer>();
		    for (Line line : clazz.getCodes()) {
		    	String lineCode = "";
		    	if(line.getType().equals("add")) {
		    		lineCode = "+ "+line.getLine();
		    		highLight.add(line.getLineNumber());
		    	}
		    	else if(line.getType().equals("delete")) {
		    		lineCode = "- "+line.getLine();
		    		highLight.add(line.getLineNumber());
		    	}else {
		    		lineCode = line.getLine();
		    	}
		    	
		    	lineCode = lineCode.replaceAll("/\\*", "/**");
		    	lineCode = lineCode.replaceAll("/\\*\\*\\*", "/**");
			    sb.append(lineCode.replace("<", "&lt;")).append("\r\n");
		    }
		    highLightList.add(highLight);
		    ClassCode classCode = new ClassCode();
		    classCode.setCode(sb.toString());
		    classCode.setClassName(clazz.getClassName());
		    codeList.add(classCode);
		}
		model.addAttribute("codeList",codeList);
		model.addAttribute("highLightList",highLightList);
		
		return "coreclass/coreclass";
    }
}