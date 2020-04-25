package edu.handong.csee.isel.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CSV2Arff {
	String input_path;
//	String output_path;
	boolean help = false;
	
	public static void main(String[] args) throws IOException {
		CSV2Arff myCSV2Arff = new CSV2Arff();
		myCSV2Arff.run(args);
	}

	private void run(String[] args) throws IOException {
		Options options = createOptions();

		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}

//			for (int i = 0; i < listOfFiles.length; i++) { // listOfFIle[i] incubator-hivemall_developer.csv
				// listOfFiles[0] is .DS_Store file
				System.out.println(input_path);
//				if (listOfFiles[i].isFile()) {
//				String inputCSVFileName = listOfFiles[i].getName();
				// /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/ace_Concat_Single_LSTM_Metric.csv
				String except_extension = input_path.split(".csv")[0];
				String outputArffFilePath = except_extension + ".arff";
//				System.out.println(inputCSVFilePath);
//				System.out.println(outputArffFilePath);

				// load CSV
				CSVLoader loader = new CSVLoader();
				// for change the label column position
				String changeColumnInputCSVFilePath = changeLabelColumn(input_path, except_extension);
				System.out.println(changeColumnInputCSVFilePath);
				loader.setSource(new File(changeColumnInputCSVFilePath));
				Instances data = loader.getDataSet();

				// save ARFF
				ArffSaver saver = new ArffSaver();
				saver.setInstances(data);
				saver.setFile(new File(outputArffFilePath));
				saver.setDestination(new File(outputArffFilePath));
				saver.writeBatch();
//				}
//			}
		}
	}
	
	public String changeLabelColumn(String inputCSVFilePath, String except_extension) throws IOException {
		String changeColumnInputCSVFilePath = except_extension + "_last_label.csv";
		BufferedWriter bufWriter = Files.newBufferedWriter(Paths.get(changeColumnInputCSVFilePath));
		//csv파일 읽기
	    List<List<String>> allData = CSVUtils.readCSV(inputCSVFilePath);
	    String label = null;
	    for(List<String> newLine : allData){
	        List<String> list = newLine;
	        for(int i = 0; i < list.size(); i++) {
	        		if (i == 0) 
	        			label = list.get(i);
	        		else {
	        			if (i == 6 || i == 11 || i == 20) continue;
	        			bufWriter.write(list.get(i));
	        			bufWriter.write(",");
	        		}	
	        }
	        bufWriter.write(label);
	        bufWriter.newLine();
	    	}
	    
	    if(bufWriter != null){
            bufWriter.close();
        }
	    
		return changeColumnInputCSVFilePath;
	}
	 
	public String deleteCVSColumns(String inputCSVFilePath, String except_extension) throws IOException {
		String deleteColumnInputCSVFilePath = "/Users/eunjiwon/Desktop/File-Level-PROMISE/" + except_extension + "_deleteColumns.csv";
		BufferedWriter bufWriter = Files.newBufferedWriter(Paths.get(deleteColumnInputCSVFilePath));
		//csv파일 읽기
	    List<List<String>> allData = CSVUtils.readCSV(inputCSVFilePath);

	    for(List<String> newLine : allData){
	        List<String> list = newLine;
	        for(int i = 0; i < list.size(); i++) {
	        		if (i < 3) 
	        			continue;
	        		if(i == list.size() - 1) { // last
	        			if(list.get(i).equals("0")) bufWriter.write("0");
	        			else bufWriter.write("1");
	        		}
	        		else {
	        			bufWriter.write(list.get(i));
	        			bufWriter.write(",");
	        		}
	        		
	        }
	        bufWriter.newLine();
	    	}
	    
	    if(bufWriter != null){
            bufWriter.close();
        }
	    
		return deleteColumnInputCSVFilePath;
	}

	Options createOptions(){

		// create Options object
		Options options = new Options();

		// add options
		options.addOption(Option.builder("i").longOpt("inputpath")
				.desc("input path")
				.hasArg()
				.argName("path")
				.required()
				.build());
		
//		options.addOption(Option.builder("o").longOpt("outputpath")
//				.desc("output path")
//				.hasArg()
//				.required()
//				.argName("path")
//				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());
		return options;
	}

	boolean parseOptions(Options options,String[] args){

		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			help = cmd.hasOption("h");
			input_path = cmd.getOptionValue("i"); 
//			output_path = cmd.getOptionValue("o"); 


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