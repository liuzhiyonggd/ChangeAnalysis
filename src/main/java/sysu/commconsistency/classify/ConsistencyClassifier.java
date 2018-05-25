package sysu.commconsistency.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import weka.classifiers.CostMatrix;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug.Random;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ConsistencyClassifier {
	private RandomForest classifier;
	private Instances instancesTrain;
	private CostSensitiveClassifier costClassifier;

	public ConsistencyClassifier() {

		classifier = new RandomForest();

		Resource resource = new ClassPathResource("file/train_1.arff");
		List<String> fileLines = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String str = null;
			while((str = br.readLine())!=null) {
				fileLines.add(str);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		File trainFile = new File("d:/data/changeanalysis/temp/consistency_train_file.arff");
		try {
			FileUtils.writeLines(trainFile, fileLines);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		};
		
		ArffLoader atf = new ArffLoader();

		try {
			atf.setFile(trainFile);
			instancesTrain = atf.getDataSet();
			instancesTrain.deleteAttributeAt(0);
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ArffLoader setFile is failed. by RamdomForest.");
		}

		costClassifier = new CostSensitiveClassifier();
		CostMatrix matrix = new CostMatrix(2);
		matrix.setCell(0, 0, 0.0d);
		matrix.setCell(0, 1, 1.0d);
		matrix.setCell(1, 0, 6.0d);
		matrix.setCell(1, 1, 0.0d);
		try {
			matrix.applyCostMatrix(instancesTrain, new Random());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		costClassifier.setCostMatrix(matrix);
		costClassifier.setClassifier(classifier);

		init(300, 100, 7);
		train();
	}

	public void init(int iterate, int percentage, int featuresNum) {
		// classifier.setBagSizePercent(percentage);
		// classifier.setNumFeatures(featuresNum);
		classifier.setNumIterations(iterate);
	}

	private void train() {
		try {
			costClassifier.buildClassifier(instancesTrain);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("train error.\n" + e.getMessage());
		}
	}

	public List<Double> classify(List<List<Double>> datas) throws Exception {

		List<Double> result = new ArrayList<Double>();

		String[] attributeNames = new String[] { "diffSize", "insertStatement", "insertStatementWeight",
				"deleteStatement", "deleteStatementWeight", "updateStatement", "updateStatementWeight", "parentChange",
				"parentChangeWeight", "orderingChange", "orderingChangeWeight", "conditionChange",
				"conditionChangeWeight", "insertAlternative", "insertAlternativeWeight", "deleteAlternative","deleteAlternativeWeight",
				"newAllLine", "oldAllLine", "subLine", "changeLine", "changeLineWeight", "firstDiffLocation",
				"tokenChange", "wordSize", "codeSimilarity", "oldCodeCommentSimilarity", "newCodeCommentSimilarity",
				"subSimilarity", "addWordNumber", "deleteWordNumber"};
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (String attrName : attributeNames) {
			Attribute attribute = new Attribute(attrName);
			attributes.add(attribute);
		}
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("0");
		labels.add("1");
		Attribute cls = new Attribute("class",labels);
		attributes.add(cls);

		Instances dataset = new Instances("test-dataset", attributes, 0);
		for (int i = 0; i < datas.size(); i++) {
			double[] attValues = new double[datas.get(i).size()];
			for (int j = 0; j < datas.get(i).size(); j++) {
				attValues[j] = datas.get(i).get(j);
			}
			Instance instance = new DenseInstance(1.0, attValues);
			dataset.add(instance);
		}
		dataset.setClassIndex(dataset.numAttributes() - 1);
		
		int num = dataset.numInstances();
		for (int i = 0; i < num; i++) {
			Double d = classifier.classifyInstance(dataset.get(i));
			result.add(d);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		
	}

}
