package edu.handong.csee.isel.weka;

import weka.attributeSelection.WrapperSubsetEval;
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
import weka.classifiers.lazy.IBk;

import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;
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

public class CrossValidationFS implements Runnable{
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
	
	public static final int EVAL_DEFAULT = 1;
	public static final int EVAL_ACCURACY = 2;
	public static final int EVAL_RMSE = 3;
	public static final int EVAL_MAE = 4;
	public static final int EVAL_FMEASURE = 5;
	public static final int EVAL_AUC = 6;
	public static final int EVAL_AUPRC = 7;
  
	public static final Tag[] TAGS_EVALUATION = {
	    new Tag(EVAL_DEFAULT, "Default: accuracy (discrete class); RMSE (numeric class)"),
	    new Tag(EVAL_ACCURACY, "Accuracy (discrete class only)"),
	    new Tag(EVAL_RMSE, "RMSE (of the class probabilities for discrete class)"),
	    new Tag(EVAL_MAE, "MAE (of the class probabilities for discrete class)"),
	    new Tag(EVAL_FMEASURE, "F-measure (discrete class only)"),
	    new Tag(EVAL_AUC, "AUC (area under the ROC curve - discrete class only)"),
	    new Tag(EVAL_AUPRC, "AUPRC (area under the precision-recall curve - discrete class only)")
	};
	  
	public CrossValidationFS(int idx, ArrayList<String> filePathList, String sourcePath, String dataUnbalancingMode, String type, String csvPath, String mlModel) {
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
			// save training data for test
//			writeADataFile(trainData,"/Users/eunjiwon/Desktop/Multucollinearity/result/test_training" + idx +".arff");
			
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
			// feature selection -> using only trainData 
			AttributeSelection attsel = new AttributeSelection();  // package weka.attributeSelection! 
			BestFirst search = new BestFirst(); 
			if(type.equals("2")) { // CFS
				CfsSubsetEval eval = new CfsSubsetEval(); 
				attsel.setEvaluator(eval); 
			}
			else if(type.equals("3")) { // LR
				WrapperSubsetEval wrapperEval = new WrapperSubsetEval();
				wrapperEval.setClassifier(new Logistic());
				wrapperEval.setEvaluationMeasure(new SelectedTag(EVAL_AUC, TAGS_EVALUATION));
				attsel.setEvaluator(wrapperEval); 
			}
			else if(type.equals("4")) { // NB
				WrapperSubsetEval wrapperEval = new WrapperSubsetEval();
				wrapperEval.setClassifier(new NaiveBayes());
				wrapperEval.setEvaluationMeasure(new SelectedTag(EVAL_AUC, TAGS_EVALUATION));
				attsel.setEvaluator(wrapperEval); 
			}
			else { // type.equals("5") kNN
				WrapperSubsetEval wrapperEval = new WrapperSubsetEval();
				Classifier knn = new IBk(10);
				wrapperEval.setClassifier(knn);
				wrapperEval.setEvaluationMeasure(new SelectedTag(EVAL_AUC, TAGS_EVALUATION));
				attsel.setEvaluator(wrapperEval); 
			}
			
			attsel.setSearch(search);
			attsel.SelectAttributes(trainData); 
//			System.out.println(attsel.toResultsString());
			
			int[] indices = attsel.selectedAttributes(); // to find feature selected index
			// trainData and testData dimension reduction
			Remove removeFilter = new Remove();
			removeFilter.setInvertSelection(true); 	// If true, leaves only the index of the array.
			removeFilter.setAttributeIndicesArray(indices);
			removeFilter.setInputFormat(trainData);
			removeFilter.setInputFormat(testData);
			trainData = Filter.useFilter(trainData, removeFilter);
			testData = Filter.useFilter(testData, removeFilter);
//			System.out.println("test " + testData.numAttributes());
//			System.out.println("train " + trainData.numAttributes());
//			System.out.println("--------------------");
			myModel.buildClassifier(trainData);
			eval_case = new Evaluation(trainData);
			eval_case.evaluateModel(myModel, testData);  
			showSummary(eval_case, trainData, mlModel, csvPath, type, testPath); 
		} catch (Exception e) {
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
