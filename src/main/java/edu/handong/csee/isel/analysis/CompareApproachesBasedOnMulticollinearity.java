package edu.handong.csee.isel.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.handong.csee.isel.weka.CSVUtils;
// at server
public class CompareApproachesBasedOnMulticollinearity {
	int dataname_col = 18;
	int approachname_col = 19; 
	static String measurementName = "";
	static String[] MLmodels = {
			"DT",			
			"LR",
			"RF",
			"NB",
			"LMT",
			"BN"
	};
	
	
	public static void main(String[] args) throws Exception {
		CompareApproachesBasedOnMulticollinearity myCABOM = new CompareApproachesBasedOnMulticollinearity();
		
		for(String ML : MLmodels) {
			String inputPath = "/home/eunjiwon/Git/EJTool/multi_results/" + ML + "_total_results.csv"; // DT_total_results.csv로 바꿔야 
			String outputPath = "/home/eunjiwon/Git/EJTool/multi_results/" + ML; // None 기준으로 다중공선성 있는 것들만 따로 모아놓음
			String approachName = "None";
	 
			String thresholdVIF = "10.0";
			ArrayList<String> listOfMulticollinearityData_10 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
			for(int i = 1; i <= 5; i++) {
				myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_10, thresholdVIF, i);
			}
			
//			thresholdVIF = "5.0";
//			ArrayList<String> listOfMulticollinearityData_5 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
//			for(int i = 1; i <= 5; i++) {
//				myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_5, thresholdVIF, i);
//			}
//		
//			thresholdVIF = "4.0";
//			ArrayList<String> listOfMulticollinearityData_4 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
//			for(int i = 1; i <= 5; i++) {
//				myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_4, thresholdVIF, i);
//			}
//			
//			thresholdVIF = "2.5";
//			ArrayList<String> listOfMulticollinearityData_2_5 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
//			for(int i = 1; i <= 5; i++) {
//				myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_2_5, thresholdVIF, i);
//			}


		}
		
		
		
		
	}
	
	// savedDataHavingMulticollinearity(inputPath, "10.0", "None")
	public ArrayList<String> savedDataHavingMulticollinearity(String inputPath, String thres, String targetApproachName){
		int idx = 0;

		if (thres.equals("10.0")) idx = 20;
		else if (thres.equals("5.0")) idx = 21;
		else if (thres.equals("4.0")) idx = 22;
		else if (thres.equals("2.5")) idx = 23;
		else {
			System.out.println("Wrong threshold of VIF");
			System.exit(-1);
		}
		
		ArrayList<String> listOfMulticollinearityData = new ArrayList<>();
		List<List<String>> allData = CSVUtils.readCSV(inputPath);  
		for(List<String> newLine : allData){
            List<String> list = newLine;
            if(list.get(approachname_col).equals(targetApproachName) && list.get(idx).equals(thres)) {
            		listOfMulticollinearityData.add(list.get(dataname_col));
            }
		}
		Double numberOfMulticollinearity = Double.valueOf(listOfMulticollinearityData.size());
		System.out.println("Input path: " + inputPath + " / Thres: " + thres + " / Approach name: " + targetApproachName + " / Number: " + listOfMulticollinearityData.size() + " / Ratio: " + numberOfMulticollinearity/4500.0);
		return listOfMulticollinearityData;
	}
	
	public void savedPerformanceOfApproaches(String inputPath, String outputPath, ArrayList<String> listOfMulticollinearityData, String thresholdVIF, int positionMeasurementColumn) throws Exception {
		
		int evaluation_measure = positionMeasurementColumn; 
		
		if(positionMeasurementColumn == 4) measurementName = "_1_AUC";
		else if(positionMeasurementColumn == 1) measurementName = "_2_Precision";
		else if(positionMeasurementColumn == 2) measurementName = "_3_Recall";
		else if(positionMeasurementColumn == 3) measurementName = "_4_Fmeasure";
		else if(positionMeasurementColumn == 5) measurementName = "_5_MCC";
		
		List<List<String>> allData = CSVUtils.readCSV(inputPath);  
		String outputCSVPath = outputPath + measurementName + "_5_multicollinearity_with_None_thres_" + thresholdVIF + ".csv";
//		FileWriter writer = new FileWriter(outputPath + measurementName + "_5_multicollinearity_with_None_thres_" + thresholdVIF + ".csv", true);
		boolean firstFlag = true;
		for(String AbsoluteSourcePath : listOfMulticollinearityData) {
			ArrayList<Double> b1List = new ArrayList<Double>();
			ArrayList<Double> b2List = new ArrayList<Double>();
			ArrayList<Double> b3List = new ArrayList<Double>();
			ArrayList<Double> b4List = new ArrayList<Double>();
			ArrayList<Double> b5List = new ArrayList<Double>();
			ArrayList<Double> b6List = new ArrayList<Double>();
			ArrayList<Double> b7List = new ArrayList<Double>();
			ArrayList<Double> b8List = new ArrayList<Double>();
			ArrayList<Double> b9List = new ArrayList<Double>();
			ArrayList<Double> b10List = new ArrayList<Double>();
			ArrayList<Double> b11List = new ArrayList<Double>(); 
			
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
				if (list.get(dataname_col).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("None")) {
					if (list.get(evaluation_measure).equals("NaN")) 
						continue;
					else 
						b1List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_PCA_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("Default-PCA")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b2List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b3List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b4List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b5List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b6List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b7List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b8List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b9List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b10List.add(Double.valueOf(list.get(evaluation_measure)));
				} 
				else if (list.get(dataname_col).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("VCRR")) { 
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b11List.add(Double.valueOf(list.get(evaluation_measure)));
				}
			}
			if(firstFlag == true) {
				saveAverageCSV(firstFlag, outputCSVPath, project + "_" + dataset, averageArray(b1List), averageArray(b2List), averageArray(b3List), averageArray(b4List), averageArray(b5List), averageArray(b6List), averageArray(b7List), averageArray(b8List), averageArray(b9List), averageArray(b10List), averageArray(b11List));
				firstFlag = false;
			}
			saveAverageCSV(firstFlag, outputCSVPath, project + "_" + dataset, averageArray(b1List), averageArray(b2List), averageArray(b3List), averageArray(b4List), averageArray(b5List), averageArray(b6List), averageArray(b7List), averageArray(b8List), averageArray(b9List), averageArray(b10List), averageArray(b11List));

		}
//		System.out.println("Finish saving the performance of approaches!");
	}
	
	public double averageArray(ArrayList<Double> list) {
		int NaNCnt = 0;
		if(list.size() == 0) {
			System.out.println("Can not average list because list size is 0, check your list!");
//			System.exit(-1);
		}
		double sum = 0;
		double average = 0;
		for(int i = 0; i < list.size(); i++) {
			if(!list.get(i).isNaN()) {
				sum += list.get(i);
			}
			else {
				NaNCnt++;
			}
		}
		average = sum / (list.size() - NaNCnt);
		return average;
	}
	
	public void saveAverageCSV(boolean headerFlag, String outputCSVPath, String dataset, Double b1Average, Double b2Average, Double b3Average, Double b4Average, Double b5Average, Double b6Average, Double b7Average, Double b8Average, Double b9Average, Double b10Average, Double b13Average) throws Exception {
		FileWriter writer =  new FileWriter(outputCSVPath, true);
		// Add header for R studio when the first data set 
		if(headerFlag) {
			ArrayList<String> baselineList = new ArrayList<String>();
			baselineList.add("");
			baselineList.add("None");
			baselineList.add("Default-PCA");
			baselineList.add("NSVIF10");
			baselineList.add("NSVIF5");
			baselineList.add("NSVIF4");
			baselineList.add("NSVIF2.5");
			baselineList.add("SVIF10");
			baselineList.add("SVIF5");
			baselineList.add("SVIF4");
			baselineList.add("SVIF2.5");
			baselineList.add("VCRR");
			CSVUtils.writeLine(writer, baselineList);
		}
		CSVUtils.writeLine(writer, Arrays.asList(dataset, String.valueOf(b1Average), String.valueOf(b2Average), String.valueOf(b3Average), String.valueOf(b4Average), String.valueOf(b5Average), String.valueOf(b6Average), String.valueOf(b7Average), String.valueOf(b8Average), String.valueOf(b9Average), String.valueOf(b10Average), String.valueOf(b13Average)));
		writer.flush();
		writer.close();
	}
	

}
