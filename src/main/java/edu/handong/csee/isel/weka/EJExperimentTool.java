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

public class EJExperimentTool implements Runnable{
	static String sourcePath;
	static String dataUnbalancingMode;
	static String type;
	static String csvPath;
	static String mlModel;
	String iter;
	String fold;
	boolean help = false;

	public static void main(String[] args) {
		EJExperimentTool myRunner = new EJExperimentTool();
		myRunner.run(args);

	}
	
	public void run(String[] args) {
		Options options = createOptions();

		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			try {
				ArrayList<String> filePathList = new ArrayList<String>();
				String path;
				// if sourcePath contain ".arff", delete extensions
				System.out.println("sourcePath is : " + sourcePath); //
				int index = sourcePath.lastIndexOf(".");
		        if (index != -1) {
		        		sourcePath = sourcePath.substring(0, index);
		        		System.out.println("file name is : " + sourcePath);
		        }
		        for(int i = 0; i < Integer.parseInt(iter); i++){
					for(int n = 0; n < Integer.parseInt(fold); n++){
						path = sourcePath + "_" + i + "_" + n + ".arff";
						filePathList.add(path);							
					}
					if(type.equals("1")) { // unsupervised
						for(int idx = 0; idx < Integer.parseInt(fold); idx++) {
							crossValidation(idx, filePathList);
						}
					}
					else if(type.equals("2")) { // supervised
						for(int idx = 0; idx < Integer.parseInt(fold); idx++) {
							crossValidationFS(idx, filePathList);
						}
					}
				
					filePathList.clear();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
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
	
	static public void crossValidation(int idx, ArrayList<String> filePathList) throws Exception {
		Instances trainData = null, testData = null, temp;
		String testPath = null;
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
//		System.out.println("test " + testData.size());
//		System.out.println("train " + trainData.size());
//		System.out.println("--------------------");
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
		Evaluation eval_case = new Evaluation(trainData);
		eval_case.evaluateModel(myModel, testData);  
		showSummary(eval_case, trainData, mlModel, csvPath, type, testPath); 
	}
	

	static public void crossValidationFS(int idx, ArrayList<String> filePathList) throws Exception {
		Instances trainData = null, testData = null, temp;
		String testPath = null;
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
//		writeADataFile(trainData,"/Users/eunjiwon/Desktop/Multucollinearity/result/test_training" + idx +".arff");
		
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
		CfsSubsetEval eval = new CfsSubsetEval(); 
		BestFirst search = new BestFirst(); 
		attsel.setEvaluator(eval); 
		attsel.setSearch(search);
		attsel.SelectAttributes(trainData); 
//		System.out.println(attsel.toResultsString()); 
		int[] indices = attsel.selectedAttributes(); // to find feature selected index
		// trainData and testData dimension reduction
		Remove removeFilter = new Remove();
		removeFilter.setInvertSelection(true); 	// If true, leaves only the index of the array.
		removeFilter.setAttributeIndicesArray(indices);
		removeFilter.setInputFormat(trainData);
		removeFilter.setInputFormat(testData);
		trainData = Filter.useFilter(trainData, removeFilter);
		testData = Filter.useFilter(testData, removeFilter);
//		System.out.println("test " + testData.numAttributes());
//		System.out.println("train " + trainData.numAttributes());
//		System.out.println("--------------------");
		myModel.buildClassifier(trainData);
		Evaluation eval_case = new Evaluation(trainData);
		eval_case.evaluateModel(myModel, testData);  
		showSummary(eval_case, trainData, mlModel, csvPath, type, testPath); 
	}
	
	// save data for test
//	static public void writeADataFile(Instances instances,String targetFilePathName) throws IOException{
//		 ArffSaver saver = new ArffSaver();
//		 saver.setInstances(instances);
//		 saver.setFile(new File(targetFilePathName));
//		 saver.writeBatch();
//	}

	public static void showSummary(Evaluation eval,Instances instances, String modelName, String csvPath, String type, String srcPath) throws Exception {
//		String csvFile = csvPath + "/result.csv";
		FileWriter writer =  new FileWriter(csvPath, true);
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
		
		options.addOption(Option.builder("d").longOpt("dataUnbalancingMode")
				.desc("1 is noHandling data unbalance or 2 is applying spread subsampling or 3 is applying smote")
				.hasArg()
				.argName("data unbalancing mode")
				.required()
				.build());

		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());

		options.addOption(Option.builder("t").longOpt("type")
				.desc("1 is unsupervised or 2 is supervised.")
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
		
		options.addOption(Option.builder("i").longOpt("iter")
				.desc("number of iteration")
				.hasArg()
				.required()
				.argName("number of cross-validation iterations")
				.build());
		
		options.addOption(Option.builder("f").longOpt("fold")
				.desc("the number of fold")
				.hasArg()
				.required()
				.argName("the number of cross-validation folds")
				.build());

		return options;
	}

	boolean parseOptions(Options options,String[] args){

		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			sourcePath = cmd.getOptionValue("s");
			dataUnbalancingMode = cmd.getOptionValue("d");
			help = cmd.hasOption("h");
			type = cmd.getOptionValue("t"); 
			csvPath = cmd.getOptionValue("c"); 
			mlModel = cmd.getOptionValue("m");
			iter = cmd.getOptionValue("i");
			fold = cmd.getOptionValue("f");


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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}


