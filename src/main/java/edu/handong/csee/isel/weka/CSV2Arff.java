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

public class CSV2Arff {
	public static void main(String[] args) throws Exception {
		CSV2Arff myCSV2Arff = new CSV2Arff();
//		File folder = new File("/Users/eunjiwon/Desktop/new_data/csv");
		String listOfFiles[] = {"bval_Add_LSTM_metric.csv",
				"bval_Add_SNGLP_metric.csv",
				"bval_developer.csv",
				"incubator-hivemall_Add_LSTM_metric.csv", 
				"incubator-hivemall_Add_SNGLP_metric.csv", 
				"incubator-hivemall_developer.csv"};
//		File[] listOfFiles = folder.listFiles();
//		System.out.println(listOfFiles);
		for (int i = 0; i < listOfFiles.length; i++) {
			// listOfFiles[0] is .DS_Store file
			System.out.println(listOfFiles[i]);
//			if (listOfFiles[i].isFile()) {
//			String inputCSVFileName = listOfFiles[i].getName();
			String inputCSVFileName = listOfFiles[i];
			String inputCSVFilePath = "/Users/eunjiwon/Desktop/new_data/csv/" + inputCSVFileName;
			String except_extension = inputCSVFileName.split(".csv")[0];
			String outputArffFilePath = "/Users/eunjiwon/Desktop/new_data/arff/" + except_extension + ".arff";
//			System.out.println(inputCSVFilePath);
//			System.out.println(outputArffFilePath);

			// load CSV
			CSVLoader loader = new CSVLoader();
			// for change the label column position
			String changeColumnInputCSVFilePath = myCSV2Arff.changeLabelColumn(inputCSVFilePath, except_extension);
			System.out.println(changeColumnInputCSVFilePath);
			loader.setSource(new File(changeColumnInputCSVFilePath));
			Instances data = loader.getDataSet();

			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(new File(outputArffFilePath));
			saver.setDestination(new File(outputArffFilePath));
			saver.writeBatch();
//			}
		}
	}
	
	public String changeLabelColumn(String inputCSVFilePath, String except_extension) throws IOException {
		String changeColumnInputCSVFilePath = "/Users/eunjiwon/Desktop/new_data/label_csv/" + except_extension + "_label.csv";
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

	
	
}