// JIT data
package edu.handong.csee.isel.weka;

import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
//import weka.classifiers.trees.RandomForest;
//import weka.classifiers.functions.Logistic;
//import weka.classifiers.trees.J48;
//import weka.classifiers.bayes.BayesNet;
//import weka.classifiers.bayes.NaiveBayes;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
//import weka.filters.unsupervised.attribute.NumericToBinary;

import java.util.Random;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class EJWekaToolApplyPCA {
	public static void main(String[] args) {
		File folder = new File("/Users/eunjiwon/Desktop/Multucollinearity/jit/removeAttributesArff/");
		File[] listOfFiles = folder.listFiles();
		for (int i = 1; i < listOfFiles.length; i++) {
			// listOfFiles[0] is .DS_Store file
			if (listOfFiles[i].isFile()) {
				String inputCSVFileName = listOfFiles[i].getName();
				System.out.println(inputCSVFileName);
				run(inputCSVFileName);
			}
		}
	}

	public static void run(String targetFile) {
		String trainingArffPath = "/Users/eunjiwon/Desktop/Multucollinearity/jit/removeAttributesArff/" + targetFile;
		try {
			// (1) read file
			BufferedReader reader = new BufferedReader(new FileReader(trainingArffPath));
			Instances trainingData = new Instances(reader);
			// set label index to last index
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			reader.close();
			
			// (2) pre-processing(ready for input data to model)
			// remove 1, 2 Attributes
//			trainingData = getInstancesByRemovingSpecificAttributes(trainingData, "1,2", false);
//			writeADataFile(trainingData,
//					"/Users/eunjiwon/Desktop/Multucollinearity/jit/removeAttributesArff/" + targetFile);
			
//			trainingData = ApplyPCA(trainingData);
			
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
			showSummary(eval, trainingData);
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

	/**
	 * Get instances with specific attributes
	 * 
	 * @param instances
	 * @param attributeIndices
	 *            attribute indices (e.g., 1,3,4) first index is 1
	 * @param invertSelection
	 *            for invert selection
	 * @return new instances with specific attributes
	 */
	static public Instances getInstancesByRemovingSpecificAttributes(Instances instances, String attributeIndices,
			boolean invertSelection) {
		Instances newInstances = new Instances(instances);

		Remove remove;

		remove = new Remove();
		remove.setAttributeIndices(attributeIndices);
		remove.setInvertSelection(invertSelection);
		try {
			remove.setInputFormat(newInstances);
			newInstances = Filter.useFilter(newInstances, remove);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		return newInstances;
	}

	/**
	 * Write an arff file with instances
	 * 
	 * @param instances
	 *            Instances object
	 * @param targetFileName
	 *            An arff file is saved with this parameter value
	 */
	static public void writeADataFile(Instances instances, String targetFileName) {
		try {
			File file = new File(targetFileName);
			if (file.exists()) {
				return;
			}

			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fos);

			dos.write((instances.toString()).getBytes());

			dos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("FileName: " + targetFileName);
			System.exit(0);
		}
	}
}
