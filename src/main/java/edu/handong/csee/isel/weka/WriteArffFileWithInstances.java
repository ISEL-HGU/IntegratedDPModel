package edu.handong.csee.isel.weka;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;

public class WriteArffFileWithInstances {
	String sourcePath;
	String type;
	String destPath;
	String mlModel;
	String vifThreshold;
	boolean help = false;
	
	public static void main(String[] args) throws Exception {
		WriteArffFileWithInstances myRunner = new WriteArffFileWithInstances();
		myRunner.run(args);
	}
	
	private void run(String[] args) throws Exception {
		Options options = createOptions();

		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			// 이제 여기다가 pca 데이터를 받아와서 그걸 arff로 쓰면 된다.
			BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
			Instances trainingData = new Instances(reader);
			// set label index to last index
			trainingData.setClassIndex(trainingData.numAttributes()-1);
			reader.close();
			
			switch(type){
				case "1": 
					trainingData = ApplyPCA(trainingData);
					writeADataFile(trainingData , destPath);
					break;
				case "2":
					trainingData = ApplyVIF(trainingData);
					writeADataFile(trainingData , destPath);
					break;
				case "3" :
					trainingData = ApplyStepwiseVIF(trainingData);
					writeADataFile(trainingData , destPath);
					break;
				default :
			}


		}
	}
	
	
	static public void writeADataFile(Instances instances,String targetFilePathName) throws IOException{
		 ArffSaver saver = new ArffSaver();
		 saver.setInstances(instances);
		 saver.setFile(new File(targetFilePathName));
		 saver.writeBatch();
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
//			System.out.println("Removed index: " + vif_max_index + " / value: " + vif_max_value);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"); 
			
			newData = ApplyStepwiseVIF(newData);
			return newData;
		}
		else {
			return instances;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newData;
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

		options.addOption(Option.builder("t").longOpt("type")
				.desc("1 is pca, 2 is non stepwise vif, 3 is stepwise vif")
				.hasArg()
				.required()
				.argName("attribute value")
				.build());

		options.addOption(Option.builder("d").longOpt("dest")
				.desc("Where to save the arff file.")
				.hasArg()
				.required()
				.argName("arff file location")
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
			help = cmd.hasOption("h");
			type = cmd.getOptionValue("t"); 
			destPath = cmd.getOptionValue("d"); 
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
