package sysu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONObject;
import sysu.commconsistency.bean.ClassMessage;
import sysu.commconsistency.bean.CodeComment;
import sysu.commconsistency.bean.CommentEntry;
import sysu.commconsistency.bean.DiffType;
import sysu.commconsistency.classify.ConsistencyClassifier;
import sysu.commconsistency.classify.DataGenerator;
import sysu.commconsistency.web.service.ClassMessageService;
import sysu.commconsistency.web.service.CommentService;
import sysu.common.util.WordSpliter;
import sysu.coreclass.bean.ClassBean;
import sysu.web.bean.ChartData;
import sysu.web.bean.CommentCode;
import sysu.web.bean.CommentInfo;
import sysu.web.bean.Data;

@Controller
public class CommentAnalysisController {

	@Autowired
	private ClassMessageService classMessageService;

	@Autowired
	private CommentService commentService;

	private static ConsistencyClassifier classifier;

	static {
		classifier = new ConsistencyClassifier();
	}

	@RequestMapping(value = "/commentstatistics/commentstatistic", method = RequestMethod.GET)
	public String commentStatistic(Model model, HttpServletRequest request) {
		int versionID = (Integer) request.getSession().getAttribute("versionID");

		List<ClassMessage> classMessageList = classMessageService.findByVersionID(versionID);
		List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
		List<Double> commentDensityList = new ArrayList<Double>();

		List<Double> commentLengthList_5 = new ArrayList<Double>();
		List<Double> commentLengthList_10 = new ArrayList<Double>();
		List<Double> commentLengthList_15 = new ArrayList<Double>();
		List<Double> commentLengthList_gt15 = new ArrayList<Double>();

		List<Double> javadocCommentList = new ArrayList<Double>();
		List<Double> blockCommentList = new ArrayList<Double>();
		List<Double> lineCommentList = new ArrayList<Double>();

		List<Double> commonCommentList = new ArrayList<Double>();
		List<Double> todoCommentList = new ArrayList<Double>();
		List<Double> bugCommentList = new ArrayList<Double>();
		List<Double> noteCommentList = new ArrayList<Double>();

		List<String> labels = new ArrayList<String>();

		for (ClassMessage clazz : classMessageList) {

			labels.add("class_" + clazz.getClassID());

			CommentInfo commentInfo = new CommentInfo();
			commentInfo.setClassID(clazz.getClassID());
			commentInfo.setClassName(clazz.getClassName());
			commentInfo.setCodeLines(clazz.getNewCode().size());

			int commentLines = 0;
			int javadocNum = 0;
			int blockNum = 0;
			int lineNum = 0;

			int commentLength_5 = 0;
			int commentLength_10 = 0;
			int commentLength_15 = 0;
			int commentLength_gt15 = 0;

			int commonComment = 0;
			int todoComment = 0;
			int bugComment = 0;
			int noteComment = 0;

			for (CodeComment codeComment : clazz.getNewComment()) {
				commentLines += codeComment.getEndLine() - codeComment.getStartLine() + 1;

				if (codeComment.getType().equals("Javadoc")) {
					javadocNum++;
				} else if (codeComment.getType().equals("Block")) {
					blockNum++;
				} else {
					lineNum++;
				}

				String comment = "";
				for (int i = codeComment.getStartLine() - 1; i < codeComment.getEndLine(); i++) {
					comment += clazz.getNewCode().get(i).getLine() + " ";
				}
				List<String> commentWords = WordSpliter.split(comment);
				int commentWordNum = WordSpliter.split(comment).size();
				if (commentWordNum <= 5) {
					commentLength_5++;
				} else if (commentWordNum <= 10) {
					commentLength_10++;
				} else if (commentWordNum <= 15) {
					commentLength_15++;
				} else {
					commentLength_gt15++;
				}

				boolean isCommonComment = true;
				for (String word : commentWords) {
					word = word.toLowerCase();
					if (word.equals("todo") || word.equals("fixme") || word.equals("xxx")) {
						todoComment++;
						isCommonComment = false;
						break;
					} else if (word.equals("bug")) {
						bugComment++;
						isCommonComment = false;
						break;
					} else if (word.equals("note")) {
						noteComment++;
						isCommonComment = false;
						break;
					}
				}
				if (isCommonComment) {
					commonComment++;
				}

			}
			commentInfo.setCommentLines(commentLines);
			commentInfo.setJavadocNum(javadocNum);
			commentInfo.setBlockNum(blockNum);
			commentInfo.setLineNum(lineNum);
			commentInfoList.add(commentInfo);

			commentLengthList_5.add((double) commentLength_5);
			commentLengthList_10.add((double) commentLength_10);
			commentLengthList_15.add((double) commentLength_15);
			commentLengthList_gt15.add((double) commentLength_gt15);

			commentDensityList.add(commentLines * 1.0d / clazz.getNewCode().size());

			javadocCommentList.add((double) javadocNum);
			blockCommentList.add((double) blockNum);
			lineCommentList.add((double) lineNum);

			commonCommentList.add((double) commonComment);
			todoCommentList.add((double) todoComment);
			bugCommentList.add((double) bugComment);
			noteCommentList.add((double) noteComment);
		}

		model.addAttribute("commentInfoList", commentInfoList);

		ChartData commentDensityChartData = new ChartData();
		commentDensityChartData.setLabels(labels);
		List<Data> commentDensityDatasets = new ArrayList<Data>();
		Data densityData = new Data();
		densityData.setBackgroundColor("rgba(72,118,255,0.8)");
		densityData.setBorderColor("rgba(72,118,255,1)");
		densityData.setData(commentDensityList);
		densityData.setLabel("注释密度");
		commentDensityDatasets.add(densityData);
		commentDensityChartData.setDatasets(commentDensityDatasets);
		JSONObject jsonCommentDensityChartData = JSONObject.fromObject(commentDensityChartData);
		model.addAttribute("commentDensityChartData", jsonCommentDensityChartData);

		ChartData commentLengthChartData = new ChartData();
		commentLengthChartData.setLabels(labels);
		List<Data> commentLengthDataset = new ArrayList<Data>();
		Data commentLengthData_5 = new Data();
		commentLengthData_5.setLabel("(0,5]");
		commentLengthData_5.setBackgroundColor("rgba(72,118,255,0.8)");
		commentLengthData_5.setBorderColor("rgba(72,118,255,1)");
		commentLengthData_5.setData(commentLengthList_5);
		commentLengthDataset.add(commentLengthData_5);

		Data commentLengthData_10 = new Data();
		commentLengthData_10.setLabel("[6,10]");
		commentLengthData_10.setBackgroundColor("rgba(121,205,205,0.8)");
		commentLengthData_10.setBorderColor("rgba(121,205,205,1)");
		commentLengthData_10.setData(commentLengthList_10);
		commentLengthDataset.add(commentLengthData_10);

		Data commentLengthData_15 = new Data();
		commentLengthData_15.setLabel("[11,15]");
		commentLengthData_15.setBackgroundColor("rgba(147,112,219,0.8)");
		commentLengthData_15.setBorderColor("rgba(147,112,219,1)");
		commentLengthData_15.setData(commentLengthList_15);
		commentLengthDataset.add(commentLengthData_15);

		Data commentLengthData_gt15 = new Data();
		commentLengthData_gt15.setLabel(">15");
		commentLengthData_gt15.setBackgroundColor("rgba(238,121,159,0.8)");
		commentLengthData_gt15.setBorderColor("rgba(238,121,159,1)");
		commentLengthData_gt15.setData(commentLengthList_gt15);
		commentLengthDataset.add(commentLengthData_gt15);
		commentLengthChartData.setDatasets(commentLengthDataset);
		JSONObject jsonCommentLengthChartData = JSONObject.fromObject(commentLengthChartData);
		model.addAttribute("commentLengthChartData", jsonCommentLengthChartData);

		ChartData commentType1ChartData = new ChartData();
		commentType1ChartData.setLabels(labels);
		List<Data> commentType1Dataset = new ArrayList<Data>();
		Data commentType1Data_todo = new Data();
		commentType1Data_todo.setLabel("TODOs");
		commentType1Data_todo.setBackgroundColor("rgba(72,118,255,0.8)");
		commentType1Data_todo.setBorderColor("rgba(72,118,255,1)");
		commentType1Data_todo.setData(todoCommentList);
		commentType1Dataset.add(commentType1Data_todo);

		Data commentType1Data_bug = new Data();
		commentType1Data_bug.setLabel("Bug");
		commentType1Data_bug.setBackgroundColor("rgba(121,205,205,0.8)");
		commentType1Data_bug.setBorderColor("rgba(121,205,205,1)");
		commentType1Data_bug.setData(bugCommentList);
		commentType1Dataset.add(commentType1Data_bug);

		Data commentType1Data_note = new Data();
		commentType1Data_note.setLabel("Note");
		commentType1Data_note.setBackgroundColor("rgba(147,112,219,0.8)");
		commentType1Data_note.setBorderColor("rgba(147,112,219,1)");
		commentType1Data_note.setData(noteCommentList);
		commentType1Dataset.add(commentType1Data_note);

		Data commmentType1Data_common = new Data();
		commmentType1Data_common.setLabel("Common");
		commmentType1Data_common.setBackgroundColor("rgba(238,121,159,0.8)");
		commmentType1Data_common.setBorderColor("rgba(238,121,159,1)");
		commmentType1Data_common.setData(commonCommentList);
		commentType1Dataset.add(commmentType1Data_common);
		commentType1ChartData.setDatasets(commentType1Dataset);
		JSONObject jsonCommentType1ChartData = JSONObject.fromObject(commentType1ChartData);
		model.addAttribute("commentType1ChartData", jsonCommentType1ChartData);

		ChartData commentType2ChartData = new ChartData();
		commentType2ChartData.setLabels(labels);
		List<Data> commentType2Dataset = new ArrayList<Data>();
		Data commentType2Data_javadoc = new Data();
		commentType2Data_javadoc.setLabel("文档注释");
		commentType2Data_javadoc.setBackgroundColor("rgba(72,118,255,0.8)");
		commentType2Data_javadoc.setBorderColor("rgba(72,118,255,1)");
		commentType2Data_javadoc.setData(javadocCommentList);
		commentType2Dataset.add(commentType2Data_javadoc);

		Data commentType2Data_block = new Data();
		commentType2Data_block.setLabel("块注释");
		commentType2Data_block.setBackgroundColor("rgba(121,205,205,0.8)");
		commentType2Data_block.setBorderColor("rgba(121,205,205,1)");
		commentType2Data_block.setData(blockCommentList);
		commentType2Dataset.add(commentType2Data_block);

		Data commentType2Data_line = new Data();
		commentType2Data_line.setLabel("行注释");
		commentType2Data_line.setBackgroundColor("rgba(147,112,219,0.8)");
		commentType2Data_line.setBorderColor("rgba(147,112,219,1)");
		commentType2Data_line.setData(lineCommentList);
		commentType2Dataset.add(commentType2Data_line);
		commentType2ChartData.setDatasets(commentType2Dataset);
		JSONObject jsonCommentType2ChartData = JSONObject.fromObject(commentType2ChartData);
		model.addAttribute("commentType2ChartData", jsonCommentType2ChartData);

		return "/commentstatistics/commentstatistic";
	}

	@RequestMapping(value = "/commentconsistency/commentconsistency", method = RequestMethod.GET)
	public String commentConsistency(Model model, HttpServletRequest request) {

		// 获取指定versionID的类列表
		int versionID = (Integer) request.getSession().getAttribute("versionID");
		List<CommentEntry> commentList = commentService.findByVersionID(versionID);

		List<List<Double>> vectors = new ArrayList<List<Double>>();
		try {
			vectors = DataGenerator.generate(versionID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Double> results = new ArrayList<Double>();
		try {
			results = classifier.classify(vectors);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < commentList.size(); i++) {
			commentList.get(i).setIsChangeProbability(results.get(i));
		}

		// 对注释列表关于不一致概率降序排序
		Collections.sort(commentList, new Comparator<CommentEntry>() {
			@Override
			public int compare(CommentEntry comment1, CommentEntry comment2) {
				return comment1.getIsChangeProbability() > comment2.getIsChangeProbability() ? -1 : 1;
			}
		});
		
		List<Set<Integer>> highlightList = new ArrayList<Set<Integer>>();
		for(CommentEntry comment:commentList) {
			Set<Integer> highLight = new TreeSet<Integer>();
			for(DiffType diff:comment.getDiffList()) {
				if(diff.getNewStartLine()>0&&diff.getNewEndLine()>0) {
					for(int i=diff.getNewStartLine();i<=diff.getNewEndLine();i++) {
						highLight.add(i);
					}
				}
			}
			highlightList.add(highLight);
		}
		
		
		List<CommentCode> commentCodeList = new ArrayList<CommentCode>();
		for(CommentEntry comment:commentList) {
			commentCodeList.add(new CommentCode(comment));
		}


		model.addAttribute("commentList", commentCodeList);
		model.addAttribute("highlightList", highlightList);

		return "commentconsistency/commentconsistency";
	}
}