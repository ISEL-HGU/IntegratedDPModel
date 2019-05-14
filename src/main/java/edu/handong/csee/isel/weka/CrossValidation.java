package edu.handong.csee.isel.weka;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;

import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.attributeSelection.AttributeSelection;

import weka.filters.unsupervised.attribute.Remove;
import weka.filters.supervised.instance.SpreadSubsample;
import weka.filters.supervised.instance.SMOTE;

import java.util.Random;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class CrossValidation implements Runnable{
	int idx;
	ArrayList<String> filePathList;
	String sourcePath;
	String dataUnbalancingMode;
	String type;
	String csvPath;
	String mlModel;
	Evaluation eval_case = null;
	Instances trainData = null;
	String testPath = null;
	
	public CrossValidation(int idx, ArrayList<String> filePathList, String sourcePath, String dataUnbalancingMode, String type, String csvPath, String mlModel) {
		this.idx = idx;
		this.filePathList = filePathList;
		this.sourcePath = sourcePath;
		this.dataUnbalancingMode = dataUnbalancingMode;
		this.type = type;
		this.csvPath = csvPath;
		this.mlModel = mlModel;
	}
	
	@Override
	public void run(){
		try {
			Instances testData = null, temp = null;
			for(int i = 0; i < filePathList.size(); i++) {
				if(i == idx) {
			    		BufferedReader reader = new BufferedReader(new FileReader(filePathList.get(i)));
			    		testData = new Instances(reader);
					reader.close();
					testPath = filePathList.get(i);
					continue;
				}
				BufferedReader reader = new BufferedReader(new FileReader(filePathList.get(i)));
		    		temp = new Instances(reader);
				reader.close();
				if(trainData == null) {
					trainData = temp;
				}
				else {
					trainData.addAll(temp);
				}
			}

			// training and prediction part
			trainData.setClassIndex(trainData.numAttributes()-1);
			testData.setClassIndex(testData.numAttributes()-1);
			
			if (dataUnbalancingMode.equals("1")) {
				// no handling unbalancing data problem
			}
			else if(dataUnbalancingMode.equals("2")) { 
				trainData = spreadSubsampling(trainData);
			}
			else if(dataUnbalancingMode.equals("3")) {
				trainData = smote(trainData);
			}
			
			Classifier myModel = (Classifier) weka.core.Utils.forName(Classifier.class, mlModel, null); 
			myModel.buildClassifier(trainData);
			eval_case = new Evaluation(trainData);
			eval_case.evaluateModel(myModel, testData);  
			showSummary(eval_case, trainData, mlModel, csvPath, type, testPath); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showSummary(Evaluation eval,Instances instances, String modelName, String csvPath, String type, String srcPath) throws Exception {
		FileWriter writer =  new FileWriter(csvPath, true);
		if(eval == null) System.out.println("showSummary - eval is null");
		else
			for(int i=0; i<instances.classAttribute().numValues()-1;i++) {
				System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
				System.out.println("Precision " + eval.precision(i));
				System.out.println("Recall " + eval.recall(i));
				System.out.println("F-Measure " + eval.fMeasure(i));
				System.out.println("AUC " + eval.areaUnderROC(i));
	
				CSVUtils.writeLine(writer, Arrays.asList(modelName, String.valueOf(eval.precision(i)), String.valueOf(eval.recall(i)), String.valueOf(eval.fMeasure(i)), String.valueOf(eval.areaUnderROC(i)), type, srcPath));
	
			}
		writer.flush();
		writer.close();
	}
	
	public static Instances spreadSubsampling(Instances trainData) throws Exception {
		// training data undersampling
		SpreadSubsample spreadsubsample = new SpreadSubsample();
		spreadsubsample.setInputFormat(trainData);
		spreadsubsample.setDistributionSpread(1.0);
		trainData = Filter.useFilter(trainData, spreadsubsample);
		return trainData;
	}
	
	public static Instances smote(Instances trainData) throws Exception {
		// smote 
		SMOTE smote = new SMOTE();
		smote.setInputFormat(trainData);
		smote.setNearestNeighbors(1);
		trainData = Filter.useFilter(trainData, smote);
		return trainData;
	}
	
}
