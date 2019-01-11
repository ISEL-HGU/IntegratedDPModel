package edu.handong.csee.isel.weka;
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
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;

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
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class EJWekaTool {
	String sourcePath;
	//	String srclabelName;
	//	String srcPosLabelValue;
	String type;
	String csvPath;
	String mlModel;
	String vifThreshold;
	boolean help = false;

	public static void main(String[] args) {

		EJWekaTool myRunner = new EJWekaTool();
		myRunner.run(args);

	}
	private void run(String[] args) {
		Options options = createOptions();

		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			System.out.print(sourcePath);
			String trainingArffPath = sourcePath;
			try {
				// "Model", "Target file", "Precision", "Recall", "F-measure", "AUC", "Type", "VIF Threshold"

				// (1) read file
				BufferedReader reader = new BufferedReader(new FileReader(trainingArffPath));
				Instances trainingData = new Instances(reader);
				// set label index to last index
				trainingData.setClassIndex(trainingData.numAttributes()-1);
				reader.close();
				
				Classifier myModel = (Classifier) weka.core.Utils.forName(Classifier.class, mlModel, null); //new Logistic();
				
				switch(type){
				case "1": 
					// original
					// (2) no pre-processing(ready for input data to model)
					myModel.buildClassifier(trainingData);
					// (4) test set and apply and prediction
					Evaluation eval_case1 = new Evaluation(trainingData);
					eval_case1.crossValidateModel(myModel, trainingData, 10, new Random(1));
					// (5) show result
					System.out.println("--------------------------");
					System.out.println(trainingArffPath);
					showSummary(eval_case1, trainingData, mlModel, trainingArffPath, csvPath, "Original", vifThreshold); 
					break;
				case "2":
					// applying PCA
					// (2) pre-processing(ready for input data to model)
					trainingData = ApplyPCA(trainingData);
					myModel.buildClassifier(trainingData);
					// (4) test set and apply and prediction
					Evaluation eval_case2 = new Evaluation(trainingData);
					eval_case2.crossValidateModel(myModel, trainingData, 10, new Random(1));
					// (5) show result
					System.out.println("--------------------------");
					System.out.println(trainingArffPath);
					showSummary(eval_case2, trainingData, mlModel, trainingArffPath, csvPath, "PCA", vifThreshold); 
					break;
				case "3" :
					// applying non stepwise VIF
					trainingData = ApplyVIF(trainingData);
					myModel.buildClassifier(trainingData);
					// (4) test set and apply and prediction
					Evaluation eval_case3 = new Evaluation(trainingData);
					eval_case3.crossValidateModel(myModel, trainingData, 10, new Random(1));
					// (5) show result
					System.out.println("--------------------------");
					System.out.println(trainingArffPath);
					showSummary(eval_case3, trainingData, mlModel, trainingArffPath, csvPath, "non stepwise VIF", vifThreshold); 
					break;
				case "4" :
					// applying stepwise VIF
					trainingData = ApplyStepwiseVIF(trainingData);
					myModel.buildClassifier(trainingData);
					// (4) test set and apply and prediction
					Evaluation eval_case4 = new Evaluation(trainingData);
					eval_case4.crossValidateModel(myModel, trainingData, 10, new Random(1));
					// (5) show result
					System.out.println("--------------------------");
					System.out.println(trainingArffPath);
					showSummary(eval_case4, trainingData, mlModel, trainingArffPath, csvPath, "stepwise VIF", vifThreshold); 
					break;
				default :
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public Instances ApplyVIF(Instances instances) throws Exception {
		double VIFThresholdValue = Double.parseDouble(vifThreshold);
		Instances newData = null;
		Instances forVIFData = null;
		ArrayList<Integer> indices = new ArrayList<>();
		Remove rm = new Remove();
		rm.setAttributeIndices("last");  
		rm.setInputFormat(instances);
		forVIFData = Filter.useFilter(instances, rm);

		int n = forVIFData.numAttributes();
//		System.out.println("n = " + n);
		double[] vifs = new double[n];
		//System.out.println("Relation: " + instances.relationName()); 
		for (int i = 0; i < vifs.length; i++) {        
			forVIFData.setClassIndex(i);
//			Using Weka Linear Regression
			AccessibleLinearRegression regressor = new AccessibleLinearRegression();
			regressor.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
			regressor.setEliminateColinearAttributes(false);
			regressor.buildClassifier(forVIFData);
			double r2 = regressor.getRSquared(forVIFData);
			
//			double[][] x = getMatrixFromInstancesRemovingClassCol(forVIFData);
////			System.out.println("--------------" + i + "--------------");
////			for (int i_ = 0; i_ < x.length; i_++) {
////	            for (int j = 0; j < x[i_].length; j++) {
////	                System.out.print(x[i_][j] + "\t"); 
////	            }
////	            System.out.println(); 
////	        }
//			double[] y = getArrayOfLabels(forVIFData);
////			System.out.println("--------------y "  + "--------------");
////			for (int i_ = 0; i_ < y.length; i_++) {
////				System.out.println(y[i_]); 
////	        }
////			
//			OLSMultipleLinearRegression OLS = new OLSMultipleLinearRegression();
////			OLS.setNoIntercept(true); //The default value for Apache Commons Math is false.
//			OLS.newSampleData(y, x);
//			double r2 = OLS.calculateRSquared();
			
//			System.out.println("R2 is : " + r2);
			vifs[i] = 1d / (1d - r2);
//			System.out.println(i + "\t" + instances.attribute(i).name() + "\t" + vifs[i]); 
			if (vifs[i] >= VIFThresholdValue) {   
				indices.add(i);
			}
		}
		int[] removingIndexArray = new int[indices.size()];
		int size = 0;
		for(int temp : indices){
			removingIndexArray[size++] = temp;
		}
		Remove removeFilter = new Remove();
		removeFilter.setAttributeIndicesArray(removingIndexArray);
		//removeFilter.setInvertSelection(true); 	// If true, leaves only the index of the array.
		removeFilter.setInputFormat(instances);
		newData = Filter.useFilter(instances, removeFilter);
		return newData;
	}
	
	public Instances ApplyStepwiseVIF(Instances instances) throws Exception {
		double VIFThresholdValue = Double.parseDouble(vifThreshold);
		Instances newData = null;
		Instances forVIFData = null;
		ArrayList<Integer> indices = new ArrayList<>();
		Remove rm = new Remove();
		rm.setAttributeIndices("last");  
		rm.setInputFormat(instances);
		forVIFData = Filter.useFilter(instances, rm);
		int vif_max_index = 0;
		double vif_max_value = 0.0;
		int n = forVIFData.numAttributes();
//		System.out.println("n = " + n);
		double[] vifs = new double[n];
		//System.out.println("Relation: " + instances.relationName()); 
		
		for (int i = 0; i < vifs.length; i++) {        
			forVIFData.setClassIndex(i);
//			Using Weka Linear Regression
			AccessibleLinearRegression regressor = new AccessibleLinearRegression();
			regressor.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
			regressor.setEliminateColinearAttributes(false);
			regressor.buildClassifier(forVIFData);
			double r2 = regressor.getRSquared(forVIFData);
//			Using apache.commons.math3.stat.regression.OLSMultipleLinearRegression
//			double[][] x = getMatrixFromInstancesRemovingClassCol(forVIFData);
//			double[] y = getArrayOfLabels(forVIFData);
//			OLSMultipleLinearRegression OLS = new OLSMultipleLinearRegression();
////			OLS.setNoIntercept(true); //The default value for Apache Commons Math is false.
//			OLS.newSampleData(y, x);
//			double r2 = OLS.calculateRSquared();
//			System.out.println("R2 is : " + r2);
			vifs[i] = 1d / (1d - r2);
//			System.out.println(i + "\t" + instances.attribute(i).name() + "\t" + vifs[i]); 
			if(vifs[i] > vif_max_value) {
				vif_max_value = vifs[i];
				vif_max_index = i;
			}
		}
		
		if(vif_max_value >= VIFThresholdValue) {
			indices.add(vif_max_index);
			int[] removingIndexArray = new int[indices.size()];
			int size = 0;
			for(int temp : indices){
				removingIndexArray[size++] = temp;
			}
			Remove removeFilter = new Remove();
			removeFilter.setAttributeIndicesArray(removingIndexArray);
			removeFilter.setInputFormat(instances);
			newData = Filter.useFilter(instances, removeFilter);
			System.out.println("Removed index: " + vif_max_index + " / value: " + vif_max_value);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"); 
			
			newData = ApplyStepwiseVIF(newData);
			return newData;
		}
		else {
			return instances;
		}
		
	}

	
//	static public double[] getArrayOfLabels(Instances instances) {
//		int classIndex = instances.classIndex();
//		double[] matrix = new double[instances.numInstances()];
//	
//		for(int i = 0; i < instances.numInstances() ;i++){
//			matrix[i] = instances.get(i).value(classIndex);
//		}
//		return matrix;
//	}
//	
//	static public double[][] getMatrixFromInstancesRemovingClassCol(Instances instances){
//		double[][] matrix = new double[instances.numInstances()][instances.numAttributes()];
//		int indexOfClassLabel = instances.classIndex();
////		System.out.println("indexOfClassLabel : " + indexOfClassLabel);
//
//		for(int i=0;i<instances.numInstances();i++){
//			for(int attrIndex=0;attrIndex<instances.numAttributes();attrIndex++){
//				if(attrIndex==indexOfClassLabel)
//					continue;
//				matrix[i][attrIndex] = instances.get(i).value(attrIndex);
//			}
//		}
//		matrix = removeCol(matrix, indexOfClassLabel);
//		return matrix;
//	}
//	
//	static public double[][] removeCol(double [][] array, int colRemove)
//	{
//	    int row = array.length;
//	    int col = array[0].length;
//
//	    double [][] newArray = new double[row][col-1]; //new Array will have one column less
//
//	    for(int i = 0; i < row; i++)
//	    {
//	        for(int j = 0,currColumn=0; j < col; j++)
//	        {
//	            if(j != colRemove)
//	            {
//	                newArray[i][currColumn++] = array[i][j];
//	            }
//	        }
//	    }
//	    return newArray;
//	}
//	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newData;
	}

	public static void showSummary(Evaluation eval,Instances instances, String modelName, String targetFile, String csvPath, String type, String threshold) throws Exception {
		String csvFile = csvPath + "/result.csv";
		FileWriter writer =  new FileWriter(csvFile, true);
		for(int i=0; i<instances.classAttribute().numValues()-1;i++) {
			System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
			System.out.println("Precision " + eval.precision(i));
			System.out.println("Recall " + eval.recall(i));
			System.out.println("F-Measure " + eval.fMeasure(i));
			System.out.println("AUC " + eval.areaUnderROC(i));

			CSVUtils.writeLine(writer, Arrays.asList(modelName, targetFile, String.valueOf(eval.precision(i)), String.valueOf(eval.recall(i)), String.valueOf(eval.fMeasure(i)), String.valueOf(eval.areaUnderROC(i)), type, threshold));

		}
		writer.flush();
		writer.close();
	}
	
	Options createOptions(){

		// create Options object
		Options options = new Options();

		// add options
		options.addOption(Option.builder("s").longOpt("source")
				.desc("Source arff file path to train a prediciton model")
				.hasArg()
				.argName("file")
				.required()
				.build());

		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());

		//		options.addOption(Option.builder("sp").longOpt("srcposlabel")
		//				.desc("String value of buggy label in source data.")
		//				.hasArg()
		//				.required()
		//				.argName("attribute value")
		//				.build());

		options.addOption(Option.builder("t").longOpt("type")
				.desc("1 is original or 2 is applying PCA or 3 is applying non stepwise VIF or 4 is applying stepwise VIF.")
				.hasArg()
				.required()
				.argName("attribute value")
				.build());

		options.addOption(Option.builder("c").longOpt("csv")
				.desc("Where to save the csv file.")
				.hasArg()
				.required()
				.argName("csv file location")
				.build());
		
		options.addOption(Option.builder("m").longOpt("model")
				.desc("Machine Learning Model")
				.hasArg()
				.required()
				.argName("machine learning model")
				.build());
		
		options.addOption(Option.builder("th").longOpt("threshold")
				.desc("VIF threshold value (type is double). If you select types 1 and 2, you will not use this value, so just write 0.")
				.hasArg()
				.required()
				.argName("vif threshold")
				.build());

		return options;
	}

	boolean parseOptions(Options options,String[] args){

		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			sourcePath = cmd.getOptionValue("s");
//			srcPosLabelValue = cmd.getOptionValue("sp");
			help = cmd.hasOption("h");
			type = cmd.getOptionValue("t"); 
			csvPath = cmd.getOptionValue("c"); 
			mlModel = cmd.getOptionValue("m");
			vifThreshold = cmd.getOptionValue("th");


		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Multicollineaity paper experiment tool";
		String footer ="\nPlease report issues at https://github.com/HGUISEL/EJTool/issues";
		formatter.printHelp("CLIExample", header, options, footer, true);
	}

}


