package sysu.web;

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
import sysu.coreclass.bean.MethodBean;
import sysu.coreclass.classify.CoreClassClassifier;
import sysu.coreclass.classify.DataGenerator;
import sysu.coreclass.web.service.ClassService;
import sysu.coreclass.web.service.MethodService;
import sysu.web.bean.ChartData;
import sysu.web.bean.ClassCode;
import sysu.web.bean.Data;
import sysu.web.bean.FormTreeData;
import sysu.web.bean.LinkData;
import sysu.web.bean.NodeData;
import sysu.web.bean.TreeCell;

/**
 * 类修改信息控制器，包含类修改统计信息页面跳转和关键类信息页面跳转。
 * @author Administrator
 *
 */
@Controller
public class ClassAnalysisController {

	@Autowired
	private ClassService classService;
	
	@Autowired
	private MethodService methodService;

	private static CoreClassClassifier coreClassClassifier;

	/*
	 * 关键类分类器初始化，在网站启动时需要消耗一些时间。
	 */
	static {
		coreClassClassifier = new CoreClassClassifier();
	}

	/**
	 * 类修改统计信息页面跳转
	 * @param model  用于传递参数
	 * @param request  获取用户当前session中的versionID
	 * @return 跳转到类修改统计信息页面
	 */
	@RequestMapping(value = "/changestatistics/changestatistic", method = RequestMethod.GET)
	public String changeStatistic(Model model, HttpServletRequest request) {

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

	/**
	 * 关键类信息页面的跳转
	 * @param model 用于传递页面参数
	 * @param request 获取当前用户中的session的versionID
	 * @return 跳转到关键类信息页面
	 */
	@RequestMapping(value = "/coreclass/coreclass", method = RequestMethod.GET)
	public String codeClass(Model model, HttpServletRequest request) {

		// 获取指定versionID的类列表
		int versionID = (Integer) request.getSession().getAttribute("versionID");
		List<List<Integer>> vectors = DataGenerator.generate(versionID);
		List<ClassBean> classList = classService.findByVersionID(versionID);

		// 类调用关系图
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
		
        //关键类判别
		List<Double> result = new ArrayList<Double>();
		try {
			result = coreClassClassifier.classify(vectors);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//关键类鱼网图
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
		
		// 对类列表关于关键类概率降序排序
		Collections.sort(classList,new Comparator<ClassBean>() {
			@Override
			public int compare(ClassBean class1,ClassBean class2) {
				return class1.getCoreProbability()>class2.getCoreProbability()?-1:1;
			}
		});
		
		// 类摘要信息
		List<String> coreClassMessageList = new ArrayList<String>();
		List<String> commonClassMessageList = new ArrayList<String>();
		
		// 如果所有类的关键类概率均小于0.5，则概率最大的那个类为关键类,并最多输出两个非关键类信息
		if(classList.get(0).getCoreProbability()<0.5d) {
			coreClassMessageList.addAll(getClassMessage(true, classList.get(0).getVersionID(), 
					classList.get(0).getClassID(), classList.get(0).getClassName()));
			
			if(classList.size()>3) {
				commonClassMessageList.addAll(getClassMessage(false,classList.get(1).getVersionID(),
						classList.get(1).getClassID(),classList.get(1).getClassName()));
				
				commonClassMessageList.addAll(getClassMessage(false,classList.get(2).getVersionID(),
						classList.get(2).getClassID(),classList.get(2).getClassName()));
			}else {
				for(int i=1;i<classList.size();i++) {
					commonClassMessageList.addAll(getClassMessage(false,classList.get(i).getVersionID(),
							classList.get(i).getClassID(),classList.get(i).getClassName()));
				}
			}
		}
		// 否则将概率大于等于0.5的类均视为关键类,关键类信息均输出，关键类不足三个时，由非关键类补充到3个。
		else {
			int count = 0;
			for(ClassBean clazz:classList) {
				if(clazz.getCoreProbability()>=0.5) {
					count++;
					coreClassMessageList.addAll(getClassMessage(true,clazz.getVersionID(),
							clazz.getClassID(),clazz.getClassName()));
					coreClassMessageList.add("");
				}else {
					break;
				}
			}
			for(;count<=3;count++) {
				commonClassMessageList.addAll(getClassMessage(false,classList.get(count).getVersionID(),
						classList.get(count).getClassID(),classList.get(count).getClassName()));
				commonClassMessageList.add("");
			}
		}
		String classMessage = "";
		for(String str:coreClassMessageList) {
			classMessage += str + "<br/>";
		}
		for(String str:commonClassMessageList) {
			classMessage += str + "<br/>";
		}
		model.addAttribute("classMessage",classMessage);
		
		// 修改类的代码列表
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
	
	private List<String> getClassMessage(boolean isCoreClass,int versionID,int classID,String className){
		List<String> classMessageList = new ArrayList<String>();
		String classType = "关键类";
		if(!isCoreClass) {
			classType = "非关键类";
			classMessageList.add("<font color='blue'>"+classType+":"+className+"</font>");
		}else {
			classMessageList.add("<font color='red' size='3'>"+classType+":"+className+"</font>");
		}
		
		String temp = "&nbsp;&nbsp;&nbsp;&nbsp;新增方法: ";
		String temp2 = "&nbsp;&nbsp;&nbsp;&nbsp;修改方法: ";
		String temp3 = "&nbsp;&nbsp;&nbsp;&nbsp;删除方法: ";
		
		List<MethodBean> methodList = methodService.findByVersionIDAndClassID(versionID, classID);
	    boolean hasNewMethod = false;
	    boolean hasChangeMethod = false;
	    boolean hasDeleteMethod = false;
		for(MethodBean method:methodList) {
	    	if(method.getMethodType().equals("new")) {
	    		hasNewMethod = true;
	    		temp += method.getMethodName()+"; ";
	    	}else if(method.getMethodType().equals("change")) {
	    		hasChangeMethod = true;
	    		temp2 += method.getMethodName() + "; ";
	    	}else {
	    		hasDeleteMethod = true;
	    		temp3 += method.getMethodName() + "; ";
	    	}
	    }
	    if(hasNewMethod) {
	        classMessageList.add(temp);
	    }
	    if(hasChangeMethod) {
	        classMessageList.add(temp2);
	    }
	    if(hasDeleteMethod) {
	        classMessageList.add(temp3);
	    }
	    return classMessageList;
	}
}