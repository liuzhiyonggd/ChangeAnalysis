package sysu.commconsistency.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.CostMatrix;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class RandomForestClassifier {
	private RandomForest classifier;
	private Instances instancesTrain;
	private Instances instancesTest;
	private CostSensitiveClassifier costClassifier;
	
	List<String> output = new ArrayList<String>();
	public List<String> getOutput(){
		return output;
	}

	public RandomForestClassifier(String trainPath,String testPath) throws FileNotFoundException, Exception {
		
		
		File inputFile = new File(trainPath);
		ArffLoader atf = new ArffLoader();
		
		File testFile = new File(testPath);
		ArffLoader atf2 = new ArffLoader();
		
		try {
			atf.setFile(inputFile);
			instancesTrain = atf.getDataSet();
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
			
			atf2.setFile(testFile);
			instancesTest = atf2.getDataSet();
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("init error.\n"+e.getMessage());
		}
		
        classifier = new RandomForest();
//        classifier.buildClassifier(instancesTrain);
		
		costClassifier = new CostSensitiveClassifier();
		CostMatrix matrix = new CostMatrix(new BufferedReader(new FileReader("D:/matrix.txt")));
		matrix.applyCostMatrix(instancesTrain, new Random());
		
		System.out.println(matrix.toString());
		costClassifier.setCostMatrix(matrix);
		costClassifier.setClassifier(classifier);
		
	}
	
	public void run(int iterate,int p,int k,String testFile,String savename){
		init(iterate,p,k);
		
		removeID();
		train();
	}

	public void init(int iterate,int percentage,int featuresNum) {
//		classifier.setBagSizePercent(percentage);
//		classifier.setNumFeatures(featuresNum);
		classifier.setNumIterations(iterate);
	}
	
	private void removeID(){
		Remove remove = new Remove();
		String[] options = new String[]{"-R","1,2"};
		try {
			remove.setOptions(options);
			remove.setInputFormat(instancesTrain);
			instancesTrain = Filter.useFilter(instancesTrain, remove);
			instancesTest = Filter.useFilter(instancesTest, remove);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void train() {
		try {
			costClassifier.buildClassifier(instancesTrain);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("train error.\n"+e.getMessage());
		}
	}
	
	public List<Double> classify(String testPath) {

		List<Double> result = new ArrayList<Double>();
		File testFile = new File(testPath);

		ArffLoader atf = new ArffLoader();

		try {
			atf.setFile(testFile);
			Instances instancesTest = atf.getDataSet();
			
			int num = instancesTest.numInstances();
			for (int i = 0; i < num; i++) {
				Double d = costClassifier.classifyInstance(instancesTest.instance(i));
				result.add(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}


	public static void main(String[] args) throws Exception {
		
	}

}
