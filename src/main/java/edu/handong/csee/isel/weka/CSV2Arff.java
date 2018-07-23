package edu.handong.csee.isel.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSV2Arff {
	/**
	 * takes 2 arguments: - CSV input file - ARFF output file
	 */
	public static void main(String[] args) throws Exception {
		File folder = new File("/Users/eunjiwon/Desktop/Multucollinearity/jit/input/");
		File[] listOfFiles = folder.listFiles();
		for (int i = 1; i < listOfFiles.length; i++) {
			// listOfFiles[0] is .DS_Store file
			if (listOfFiles[i].isFile()) {
				String inputCSVFileName = listOfFiles[i].getName();
				String inputCSVFilePath = "/Users/eunjiwon/Desktop/Multucollinearity/jit/input/" + inputCSVFileName;
				String outputArffFilePath = "/Users/eunjiwon/Desktop/Multucollinearity/jit/arff/" + inputCSVFileName
						+ ".arff";
//				System.out.println(inputCSVFilePath);
//				System.out.println(outputArffFilePath);

				// load CSV
				CSVLoader loader = new CSVLoader();
				loader.setSource(new File(inputCSVFilePath));
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
}