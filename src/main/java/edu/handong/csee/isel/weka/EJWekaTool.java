package edu.handong.csee.isel.weka;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
//import weka.classifiers.functions.Logistic;
//import weka.classifiers.trees.J48;
//import weka.classifiers.bayes.BayesNet;
//import weka.classifiers.bayes.NaiveBayes;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.util.Random;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EJWekaTool {

	public static void main(String[] args) {
		String[] targetFilelist = { "Relink/Apache.arff", "Relink/Safe.arff", "Relink/Zxing.arff", "AEEEM/EQ.arff", "AEEEM/JDT.arff", "AEEEM/LC.arff", "AEEEM/ML.arff", "AEEEM/PDE.arff" };
		for(int i = 0; i < targetFilelist.length; i++) {
			run(targetFilelist[i]);
		}
		
	}

	public static void run(String targetFile) {
		
		String trainingArffPath = "/Users/eunjiwon/Desktop/Multucollinearity/TransferDefectLearningDataset/Original/" + targetFile;
		try {
			// (1) read file
			BufferedReader reader = new BufferedReader(new FileReader(trainingArffPath));
			Instances trainingData = new Instances(reader);
			// set label index to last index
			trainingData.setClassIndex(trainingData.numAttributes()-1);
			reader.close();
			// (2) no pre-processing(ready for input data to model)
			
			trainingData = ApplyPCA(trainingData);
			
			// (3) make model
//			Classifier myModel = new Logistic(); 
//			Classifier myModel = new J48();
//			Classifier myModel = new BayesNet();
//			Classifier myModel = new NaiveBayes();
//			Classifier myModel = new RandomForest();
			Classifier myModel = new SMO();
			
			myModel.buildClassifier(trainingData);
			// (4) test set and apply and prediction
			Evaluation eval = new Evaluation(trainingData);
			eval.crossValidateModel(myModel, trainingData, 10, new Random(1));
			// (5) show result
			System.out.println("--------------------------");
			System.out.println(targetFile);
			showSummary(eval,trainingData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showSummary(Evaluation eval,Instances instances) {
		// Class True
		for(int i=0; i<instances.classAttribute().numValues()-1;i++) {
			System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
			System.out.println("Precision " + eval.precision(i));
			System.out.println("Recall " + eval.recall(i));
			System.out.println("F-Measure " + eval.fMeasure(i));
			System.out.println("AUC " + eval.areaUnderROC(i));
		}
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
