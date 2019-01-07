package edu.handong.csee.isel.weka;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
//import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
//import weka.classifiers.bayes.BayesNet;
//import weka.classifiers.bayes.NaiveBayes;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.util.Random;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileWriter;
import java.util.Arrays;

import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class EJExperimentTool {
	static String type = "2";
	static String rootDir = "/Users/eunjiwon/Desktop/Multucollinearity/Data/";
	public static void main(String[] args) throws IOException {
		File folder = new File(rootDir);
		File[] listOfFiles = folder.listFiles();
		
		String csvFile = rootDir + "test.csv";
		FileWriter writer =  new FileWriter(csvFile, true);
		CSVUtils.writeLine(writer, Arrays.asList("Model", "Targer file", "Precision", "Recall", "F-measure", "AUC", "Type"));
		writer.flush();
	    writer.close();
	    
		for (int i = 1; i < listOfFiles.length; i++) {
		// 인덱스를 다시 1부터 해야하나보다... DS_Store 이것도 나중에 파일별로 바꾸면서 고쳐질 것이다
		//for (int i = 1; i < 2; i++) {
			if (listOfFiles[i].isFile()) {
				String inputFileName = listOfFiles[i].getName();
				System.out.println(inputFileName);
				run(inputFileName, "Decision Tree");
			}
		}
		
	}

	public static void run(String targetFile, String modelName) {
		
		String trainingArffPath = rootDir + targetFile;
		try {
			// (1) read file
			BufferedReader reader = new BufferedReader(new FileReader(trainingArffPath));
			Instances trainingData = new Instances(reader);
			// set label index to last index
			trainingData.setClassIndex(trainingData.numAttributes()-1);
			reader.close();
			
			// (3) make model
//			Classifier myModel = new Logistic(); 
			Classifier myModel = new J48();
//			Classifier myModel = new BayesNet();
//			Classifier myModel = new NaiveBayes();
//			Classifier myModel = new RandomForest();
//			Classifier myModel = new SMO();
			
		    switch(type){
	        case "1": 
	        		// without PCA
	        		// (2) no pre-processing(ready for input data to model)
		        	myModel.buildClassifier(trainingData);
	    			// (4) test set and apply and prediction
	    			Evaluation eval_case1 = new Evaluation(trainingData);
	    			eval_case1.crossValidateModel(myModel, trainingData, 10, new Random(1));
	    			// (5) show result
	    			System.out.println("--------------------------");
	    			System.out.println(targetFile);
	    			showSummary(eval_case1,trainingData, modelName, targetFile); // 나중에 타입을 인자로 넣어서 보낼 수도 있음  
	            break;
	        case "2":
	        		// within PCA
	        		// (2) pre-processing(ready for input data to model)
	        		trainingData = ApplyPCA(trainingData);
	    			myModel.buildClassifier(trainingData);
	    			// (4) test set and apply and prediction
	    			Evaluation eval_case2 = new Evaluation(trainingData);
	    			eval_case2.crossValidateModel(myModel, trainingData, 10, new Random(1));
	    			// (5) show result
	    			System.out.println("--------------------------");
	    			System.out.println(targetFile);
	    			showSummary(eval_case2,trainingData, modelName, targetFile); // 나중에 타입을 인자로 넣어서 보낼 수도 있음  
	            break;
	        case "3" :
	        		// within VIF
	          
	            break;
	        default :
	            // 이런 경우는 없어야 할 듯  
		    }
			
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showSummary(Evaluation eval,Instances instances, String modelName, String targetFile) throws Exception {
		// Class True
		String csvFile = rootDir + "test.csv";
		FileWriter writer =  new FileWriter(csvFile, true);
		for(int i=0; i<instances.classAttribute().numValues()-1;i++) {
			System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
			System.out.println("Precision " + eval.precision(i));
			System.out.println("Recall " + eval.recall(i));
			System.out.println("F-Measure " + eval.fMeasure(i));
			System.out.println("AUC " + eval.areaUnderROC(i));

			CSVUtils.writeLine(writer, Arrays.asList(modelName, targetFile, String.valueOf(eval.precision(i)), String.valueOf(eval.recall(i)), String.valueOf(eval.fMeasure(i)), String.valueOf(eval.areaUnderROC(i)), type));
	
		}
		writer.flush();
	    writer.close();
	}
	
	static public Instances ApplyPCA(Instances data) {
		Instances newData = null;
		Ranker ranker = new Ranker();
		AttributeSelection filter = new AttributeSelection(); // package weka.filters.supervised.attribute!
		PrincipalComponents eval = new PrincipalComponents();
		// eval.setVarianceCovered(1.0);
		filter.setEvaluator(eval);
		try {
			filter.setInputFormat(data);
			filter.setSearch(ranker); // add ranker
			// generate new data
			newData = Filter.useFilter(data, filter);
//			System.out.println(eval.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newData;
	}

}


