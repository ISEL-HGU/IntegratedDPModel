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
		File folder = new File("/Users/eunjiwon/Desktop/File-Level-PROMISE/");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			// listOfFiles[0] is .DS_Store file
			if (listOfFiles[i].isFile()) {
				String inputCSVFileName = listOfFiles[i].getName();
				String inputCSVFilePath = "/Users/eunjiwon/Desktop/File-Level-PROMISE/" + inputCSVFileName;
				String except_extension = inputCSVFileName.split(".csv")[0];
				String outputArffFilePath = "/Users/eunjiwon/Desktop/File-Level-PROMISE/arff/PROMISE_" + except_extension + ".arff";
				System.out.println(inputCSVFilePath);
				System.out.println(outputArffFilePath);

				// load CSV
				CSVLoader loader = new CSVLoader();
				// for remove first three columns in PROMISE dataset
				String delColInputCSVFilePath = myCSV2Arff.deleteCVSColumns(inputCSVFilePath, except_extension);
				System.out.println(delColInputCSVFilePath);
				loader.setSource(new File(delColInputCSVFilePath));
				Instances data = loader.getDataSet();

				// save ARFF
				ArffSaver saver = new ArffSaver();
				saver.setInstances(data);
				saver.setFile(new File(outputArffFilePath));
				saver.setDestination(new File(outputArffFilePath));
				saver.writeBatch();
			}
		}
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