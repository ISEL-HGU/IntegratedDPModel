package edu.handong.csee.isel.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.handong.csee.isel.weka.CSVUtils;

public class CompareApproachesBasedOnMulticollinearity {

	public static void main(String[] args) throws IOException {
		CompareApproachesBasedOnMulticollinearity myCABOM = new CompareApproachesBasedOnMulticollinearity();
		String inputPath = "/home/eunjiwon/Git/EJTool/checkedMulticollinearity_DT.csv";
		String outputPath = "/home/eunjiwon/Git/EJTool/multi_results/compare_CFS_DT.csv";
		String approachName = "CFS-BestFirst";
		// 
		String thresholdVIF = "10.0";
		ArrayList<String> listOfMulticollinearityData_10 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
		myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_10, thresholdVIF);
		//
		thresholdVIF = "5.0";
		ArrayList<String> listOfMulticollinearityData_5 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
		myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_5, thresholdVIF);
		//
		thresholdVIF = "4.0";
		ArrayList<String> listOfMulticollinearityData_4 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
		myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_4, thresholdVIF);
		//
		thresholdVIF = "2.5";
		ArrayList<String> listOfMulticollinearityData_2_5 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
		myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_2_5, thresholdVIF);
	}
	
	// savedDataHavingMulticollinearity(inputPath, "10.0", "None")
	public ArrayList<String> savedDataHavingMulticollinearity(String inputPath, String thres, String targetApproachName){
		int idx = 0;
		if (thres.equals("10.0")) idx = 8;
		else if (thres.equals("5.0")) idx = 9;
		else if (thres.equals("4.0")) idx = 10;
		else if (thres.equals("2.5")) idx = 11;
		else {
			System.out.println("Wrong threshold of VIF");
			System.exit(-1);
		}
		
		ArrayList<String> listOfMulticollinearityData = new ArrayList<>();
		List<List<String>> allData = CSVUtils.readCSV(inputPath);  
		for(List<String> newLine : allData){
            List<String> list = newLine;
            if(list.get(7).equals(targetApproachName) && list.get(idx).equals(thres)) {
            		listOfMulticollinearityData.add(list.get(6));
            }
		}
		Double numberOfMulticollinearity = Double.valueOf(listOfMulticollinearityData.size());
		System.out.println("Thres: " + thres + " / Approach name: " + targetApproachName + " / Number: " + listOfMulticollinearityData.size() + " / Ratio: " + numberOfMulticollinearity/4500.0);
		return listOfMulticollinearityData;
	}
	
	public void savedPerformanceOfApproaches(String inputPath, String outputPath, ArrayList<String> listOfMulticollinearityData, String thresholdVIF) throws IOException {
		List<List<String>> allData = CSVUtils.readCSV(inputPath);  
		FileWriter writer = new FileWriter(outputPath, true);
		
		for(String AbsoluteSourcePath : listOfMulticollinearityData) {
			// extract detail information
			File file = new File(AbsoluteSourcePath); // absolute path
			String multicollinearity_sourcepath = file.getName(); // only file name
			multicollinearity_sourcepath = multicollinearity_sourcepath.replace(".arff", ""); 
			String[] multicollinearity_sourcepath_array;
			multicollinearity_sourcepath_array = multicollinearity_sourcepath.split("_");
			String project = multicollinearity_sourcepath_array[0];
			String dataset = multicollinearity_sourcepath_array[1];
			String iter = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 2];
			String fold = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 1];
//			System.out.println("project: " + project + " / dataset: " + dataset + " / iter: " + iter + " / fold: " + fold);
			// search the dataset in the inputPath
			for (List<String> newLine : allData) {
				List<String> list = newLine;
				if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(7).equals("None")) {
					if (list.get(4).equals("NaN")) 
						continue;
					else 
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "None"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_PCA_" + iter + "_" + fold + ".arff") && list.get(7).equals("Default-PCA")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "Default-PCA"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF10")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "NSVIF10"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF5")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "NSVIF5"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF4")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "NSVIF4"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("NSVIF2.5")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "NSVIF2.5"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF10")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "SVIF10"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF5")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "SVIF5"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF4")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "SVIF4"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_VIF_STEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(7).equals("SVIF2.5")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "SVIF2.5"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(7).equals("CFS-BestFirst")) {
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "CFS-BestFirst"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(7).equals("WFS-BestFirst")) { 
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "WFS-BestFirst"));
				} 
				else if (list.get(6).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(7).equals("VCRR")) { 
					if (list.get(4).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(4))), "VCRR"));
				}
			}

		}
		writer.flush();
		writer.close();	
		System.out.println("Finish saving the performance of approaches!");
	}
	
	

}
