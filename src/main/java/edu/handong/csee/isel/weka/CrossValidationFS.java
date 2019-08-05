package edu.handong.csee.isel.weka;

import edu.handong.csee.isel.analysis.AverageArray;
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
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import weka.core.converters.CSVSaver;

public class CrossValidationFS implements Runnable {
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
	public void run() {
		String isMulticollinearity = "";
		
		try {
			Instances testData = null, temp = null;
			for (int i = 0; i < filePathList.size(); i++) {
				if (i == idx) {
					BufferedReader reader = new BufferedReader(new FileReader(filePathList.get(i)));
					testData = new Instances(reader);
					reader.close();
					testPath = filePathList.get(i);
					continue;
				}
				BufferedReader reader = new BufferedReader(new FileReader(filePathList.get(i)));
				temp = new Instances(reader);
				reader.close();
				if (trainData == null) {
					trainData = temp;
				} else {
					trainData.addAll(temp);
				}

			}
			// save training data for test
			// writeADataFile(trainData,"/Users/eunjiwon/Desktop/Multucollinearity/result/test_training"
			// + idx +".arff");

			// training and prediction part
			trainData.setClassIndex(trainData.numAttributes() - 1);
			testData.setClassIndex(testData.numAttributes() - 1);

			if (dataUnbalancingMode.equals("1")) {
				// no handling unbalancing data problem
			} else if (dataUnbalancingMode.equals("2")) {
				trainData = spreadSubsampling(trainData);
			} else if (dataUnbalancingMode.equals("3")) {
				trainData = smote(trainData);
			}

			// Save original test data as CSV file
			// CSVSaver saver = new CSVSaver();
			// saver.setInstances(testData);
			// saver.setFile(new File("/Users/eunjiwon/Desktop/arff/arff.csv"));
			// saver.writeBatch();

			Classifier myModel = (Classifier) weka.core.Utils.forName(Classifier.class, mlModel, null);
			// feature selection -> using only trainData
			AttributeSelection attsel = new AttributeSelection(); // package weka.attributeSelection!
			BestFirst search = new BestFirst();
			if (type.equals("2")) { // CFS
				CfsSubsetEval eval = new CfsSubsetEval();
				attsel.setEvaluator(eval);
			} else if (type.equals("3")) { // WFS
				WrapperSubsetEval wrapperEval = new WrapperSubsetEval();
				wrapperEval.setClassifier(myModel); // It is the same ML model as the prediction ML model
				wrapperEval.setEvaluationMeasure(new SelectedTag(EVAL_AUC, TAGS_EVALUATION));
				attsel.setEvaluator(wrapperEval);
			}

			attsel.setSearch(search);
			attsel.SelectAttributes(trainData);
//			System.out.println(attsel.toResultsString());

			int[] indices = attsel.selectedAttributes(); // to find feature selected index
			// trainData and testData dimension reduction
			Remove removeFilter = new Remove();
			removeFilter.setInvertSelection(true); // If true, leaves only the index of the array.
			removeFilter.setAttributeIndicesArray(indices);
			removeFilter.setInputFormat(trainData);
			removeFilter.setInputFormat(testData);
			trainData = Filter.useFilter(trainData, removeFilter);
			testData = Filter.useFilter(testData, removeFilter);
			// System.out.println("test " + testData.numAttributes());
			// System.out.println("train " + trainData.numAttributes());
			// System.out.println("--------------------");

			// Save the CFS, WFS features number
			// saveFeaturesNumber(mlModel, csvPath, type, testPath,trainData.numAttributes());

			// Check the multicollinearity to train data using VIF & save the source path to arraylist
			File file = new File(testPath); // absolute path
			String fileName = file.getName(); // only file name
			isMulticollinearity = checkMulticollinearity(trainData, 10.0);
			if (isMulticollinearity.equals("Y")) {
				saveMulticollinearityWFSCompareTheOtherApporoachesResults(mlModel, csvPath, type, testPath, "10.0", fileName);
			}
			
			isMulticollinearity = checkMulticollinearity(trainData, 5.0);
			if (isMulticollinearity.equals("Y")) {
				saveMulticollinearityWFSCompareTheOtherApporoachesResults(mlModel, csvPath, type, testPath, "5.0", fileName);
			}
			
			isMulticollinearity = checkMulticollinearity(trainData, 4.0);
			if (isMulticollinearity.equals("Y")) {
				saveMulticollinearityWFSCompareTheOtherApporoachesResults(mlModel, csvPath, type, testPath, "4.0", fileName);
			}
			
			isMulticollinearity = checkMulticollinearity(trainData, 2.5);
			if (isMulticollinearity.equals("Y")) {
				saveMulticollinearityWFSCompareTheOtherApporoachesResults(mlModel, csvPath, type, testPath, "2.5", fileName);
			}
				
			// myModel.buildClassifier(trainData);
			// eval_case = new Evaluation(trainData);
			// eval_case.evaluateModel(myModel, testData);
			// showSummary(eval_case, trainData, mlModel, csvPath, type, testPath,
			// isMulticollinearity);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void saveMulticollinearityWFSCompareTheOtherApporoachesResults(String modelName, String csvPath, String type, String srcPath, String vif_threshold,
			String multicollinearity_sourcepath) throws IOException {

		List<List<String>> allData = CSVUtils.readCSV("/home/eunjiwon/Git/EJTool/multi_results/DT_total_results_baseline.csv"); // AUC 값을 얻어올 수 있음 
//		List<List<String>> allData = CSVUtils.readCSV("/Users/eunjiwon/Desktop/exp_results/0726_exp_results/DT_total_results_baseline.csv"); // for local test
		
		FileWriter writer = new FileWriter(csvPath, true);
		
		multicollinearity_sourcepath = multicollinearity_sourcepath.replace(".arff", ""); 
		String[] multicollinearity_sourcepath_array;
		multicollinearity_sourcepath_array = multicollinearity_sourcepath.split("_");
		String project = multicollinearity_sourcepath_array[0];
		String dataset = multicollinearity_sourcepath_array[1];
		String iter = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 2];
		String fold = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 1];
		for (List<String> newLine : allData) {
			List<String> list = newLine;
			if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(7).equals("None")) {
				if (list.get(4).equals("NaN")) 
					continue;
				else 
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "None"));
			} else if (list.get(6).contains(project + "_" + dataset + "_PCA_" + iter + "_" + fold + ".arff") && list.get(7).equals("Default-PCA")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "Default-PCA"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF10")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "NSVIF10"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF5")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "NSVIF5"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF4")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "NSVIF4"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF2.5")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "NSVIF2.5"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF10")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "SVIF10"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF5")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "SVIF5"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF4")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "SVIF4"));
			} else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF2.5")) {
				if (list.get(4).equals("NaN"))
					continue;
				else
					CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "SVIF2.5"));
			} else if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(8).equals("CFS-BestFirst")) { // type2
				if (list.get(4).equals("NaN"))
					continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "CFS-BestFirst"));
				} else if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(8).equals("WFS-BestFirst")) { // type3
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, vif_threshold, String.valueOf(Double.valueOf(list.get(4))), "WFS-BestFirst"));
				}
			}

		
		writer.flush();
		writer.close();	
	}

	public static void saveFeaturesNumber(String modelName, String csvPath, String type, String srcPath, int numAttributes) throws IOException {
		FileWriter writer = new FileWriter(csvPath, true);
		CSVUtils.writeLine(writer, Arrays.asList(modelName, type, srcPath, String.valueOf(numAttributes)));
		writer.flush();
		writer.close();
	}

	public static void showSummary(Evaluation eval, Instances instances, String modelName, String csvPath, String type, String srcPath, String isMulticollinearity) throws Exception {
		FileWriter writer = new FileWriter(csvPath, true);
		if (eval == null)
			System.out.println("showSummary - eval is null");
		else
			for (int i = 0; i < instances.classAttribute().numValues() - 1; i++) {
				System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
				System.out.println("Precision " + eval.precision(i));
				System.out.println("Recall " + eval.recall(i));
				System.out.println("F-Measure " + eval.fMeasure(i));
				System.out.println("AUC " + eval.areaUnderROC(i));

				CSVUtils.writeLine(writer, Arrays.asList(modelName, String.valueOf(eval.precision(i)), String.valueOf(eval.recall(i)), String.valueOf(eval.fMeasure(i)),
						String.valueOf(eval.areaUnderROC(i)), type, srcPath, isMulticollinearity));

			}
		writer.flush();
		writer.close();
	}

	public String checkMulticollinearity(Instances instances, double VIFThresholdValue) throws Exception {
		String isMulticollinearity = "";
		Instances forVIFData = null;
		Remove rm = new Remove();
		rm.setAttributeIndices("last");
		rm.setInputFormat(instances);
		forVIFData = Filter.useFilter(instances, rm);
		int n = forVIFData.numAttributes();
		double[] vifs = new double[n];
		for (int i = 0; i < vifs.length; i++) {
			forVIFData.setClassIndex(i);
			// Using Weka Linear Regression
			AccessibleLinearRegression regressor = new AccessibleLinearRegression();
			regressor.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
			regressor.setEliminateColinearAttributes(false);
			regressor.buildClassifier(forVIFData);
			double r2 = regressor.getRSquared(forVIFData);
			vifs[i] = 1d / (1d - r2);
			if (vifs[i] >= VIFThresholdValue) {
				isMulticollinearity = "Y"; // Occur multicollinearity
				break;
			} else {
				isMulticollinearity = "N";
			}
		}
		return isMulticollinearity;
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
