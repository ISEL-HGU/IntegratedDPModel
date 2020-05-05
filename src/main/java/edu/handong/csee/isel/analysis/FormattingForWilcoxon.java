package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.handong.csee.isel.weka.CSVUtils;

public class FormattingForWilcoxon {
//	static String path = "/Users/eunjiwon/Desktop/NGLP_Results/master_model_2019_06_30_NGLPBugPatchCollector_Results/";
	static String path = "/Users/eunjiwon/Desktop/";
	static String[] filenameArray = { "Origin_SLSTMConcat_SLSTMLine_SNGLP_MNGLP_LR_result.csv" };
//	static String[] filenameArray = { "nglp_RF_result.csv", "nglp_DT_result.csv", "nglp_LR_result.csv", "nglp_NB_result.csv" };
	// for statistical test based on project
//	static String path = "/Users/eunjiwon/Desktop/NGLP_Results/Based_on_project_statistical_test/";
//	static String[] filenameArray = { "MasterNGLPvsBaseline_result.csv" };
//	
//	static String MasterNGLPPath = path + "MasterNGLPvsBaseline_result.csv";
//	static String SingleNGLPPath = path + "SingleNGLPvsBaseline_result.csv";
	
	public static void main(String[] args) throws IOException {
		FormattingForWilcoxon myFormattingForWilcoxon = new FormattingForWilcoxon();
		for (String filename : filenameArray) {
			myFormattingForWilcoxon.run_LSTM(filename);
//			myFormattingForWilcoxon.run(filename);
		}
	}
	public void run_LSTM(String baselinePath) {

		String[] dataset = {"ace","ant-ivy","bigtop","bval","camel","cayenne","cordova-android","creadur-rat","crunch","deltaspike","gora","groovy","guacamole-client","incubator-hivemall"};
//		String[] dataset = { "camel", "eagle", "flink", "groovy", "jena", "juddi", "metamodel", "nutch" };
		int precision_col = 1;
		int recall_col = 2;
		int fmeasure_col = 3;
		int auc_col = 4;
		int type_col = 5;
		int dataname_col = 6;

		List<List<String>> allData = readCSV(path + baselinePath);

		for (String datasetName : dataset) {
			ArrayList<Double> LSTMList_AUC = new ArrayList<Double>();
			ArrayList<Double> LSTMList_FMeasure = new ArrayList<Double>();
			ArrayList<Double> LSTMList_Precision = new ArrayList<Double>();
			ArrayList<Double> LSTMList_Recall = new ArrayList<Double>();
			ArrayList<Double> NGLPList_AUC = new ArrayList<Double>();
			ArrayList<Double> NGLPList_FMeasure = new ArrayList<Double>();
			ArrayList<Double> NGLPList_Precision = new ArrayList<Double>();
			ArrayList<Double> NGLPList_Recall = new ArrayList<Double>();
			for (List<String> newLine : allData) {
				List<String> list = newLine;
				String dataset_name = list.get(dataname_col);
				String type_name = list.get(type_col);
				if (dataset_name.contains(datasetName)){
					if (dataset_name.contains("developer")) {
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else NGLPList_AUC.add(Double.valueOf(list.get(auc_col)));
						// Precision
						if (list.get(precision_col).equals("NaN")) continue;
						else NGLPList_Precision.add(Double.valueOf(list.get(precision_col)));
						// Recall
						if (list.get(recall_col).equals("NaN")) continue;
						else NGLPList_Recall.add(Double.valueOf(list.get(recall_col)));
						// F-Measure
						if (list.get(fmeasure_col).equals("NaN")) continue;
						else NGLPList_FMeasure.add(Double.valueOf(list.get(fmeasure_col)));
					}
					else if(dataset_name.contains("Line_Single_LSTM_Metric")){ // LSTM dataset 
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else LSTMList_AUC.add(Double.valueOf(list.get(auc_col)));
						// Precision
						if (list.get(precision_col).equals("NaN")) continue;
						else LSTMList_Precision.add(Double.valueOf(list.get(precision_col)));
						// Recall
						if (list.get(recall_col).equals("NaN")) continue;
						else LSTMList_Recall.add(Double.valueOf(list.get(recall_col)));
						// F-Measure
						if (list.get(fmeasure_col).equals("NaN")) continue;
						else LSTMList_FMeasure.add(Double.valueOf(list.get(fmeasure_col)));
					}
				
				}
						
			}
			try {
				// for statistical test based on average value
				saveAverageCSV_LSTM(baselinePath, datasetName, averageArray(LSTMList_AUC), averageArray(LSTMList_Precision), averageArray(LSTMList_Recall), averageArray(LSTMList_FMeasure), averageArray(NGLPList_AUC), averageArray(NGLPList_Precision), averageArray(NGLPList_Recall), averageArray(NGLPList_FMeasure));
				// for statistical test based on each project 
//				saveEachProjectCSV(baselinePath, datasetName, NGLPList_AUC, LSTMList_AUC); // baselinePath is "SingleNGLPvsBaseline_result.csv"
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error writing average to csv");
			}

		}

	}
	public static void saveAverageCSV_LSTM(String baselinePath, String dataset, Double LSTMList_AUC_Average, Double LSTMList_Precision_Average, Double LSTMList_Recall_Average, Double LSTMList_FMeasure_Average, Double NGLPList_AUC_Average, Double NGLPList_Precision_Average, Double NGLPList_Recall_Average,
			Double NGLPList_FMeasure_Average) throws Exception {
		FileWriter writer = new FileWriter(path + "Line_Single_LSTM_Metric_vs_Origin" + "_average.csv", true);
		// Add header for R studio when the first data set
		if (dataset.equals("ace")) {
			ArrayList<String> baselineList = new ArrayList<String>();
			baselineList.add("");
			baselineList.add("Line_Single_LSTM_Metric_AUC");
			baselineList.add("Line_Single_LSTM_Metric_Precision");
			baselineList.add("Line_Single_LSTM_Metric_Recall");
			baselineList.add("Line_Single_LSTM_Metric_FMeasure");
			baselineList.add("Origin_AUC");
			baselineList.add("Origin_Precision");
			baselineList.add("Origin_Recall");
			baselineList.add("Origin_FMeasure");
			CSVUtils.writeLine(writer, baselineList);
		}
		CSVUtils.writeLine(writer,
				Arrays.asList(dataset, String.valueOf(LSTMList_AUC_Average), String.valueOf(LSTMList_Precision_Average), String.valueOf(LSTMList_Recall_Average), String.valueOf(LSTMList_FMeasure_Average), String.valueOf(NGLPList_AUC_Average), String.valueOf(NGLPList_Precision_Average),
						String.valueOf(NGLPList_Recall_Average), String.valueOf(NGLPList_FMeasure_Average)));
		writer.flush();
		writer.close();
	}

	public void compareBetweenMasterAndSingle(String masterNGLPPath, String singleNGLPPath) {
		// TODO Auto-generated method stub
		ArrayList<Double> MasterList_AUC = new ArrayList<Double>();
		ArrayList<Double> SingleList_AUC = new ArrayList<Double>();
		String[] dataset = { "camel", "eagle", "flink", "groovy", "jena", "juddi", "metamodel", "nutch" };
		int auc_col = 4;
		int dataname_col = 6;
		List<List<String>> MasterAllData = readCSV(masterNGLPPath); 
		List<List<String>> SingleAllData = readCSV(singleNGLPPath); 
		
		for (String datasetName : dataset) {
			// Master
			for (List<String> newLine : MasterAllData) {
				List<String> list = newLine;
				String dataset_name = list.get(dataname_col);
				if (dataset_name.contains(datasetName)){
					if (dataset_name.contains("NGLP")) {
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else MasterList_AUC.add(Double.valueOf(list.get(auc_col)));
						
					}
				}		
			}
			// Single
			for (List<String> newLine : SingleAllData) {
				List<String> list = newLine;
				String dataset_name = list.get(dataname_col);
				if (dataset_name.contains(datasetName)){
					if (dataset_name.contains("NGLP")) {
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else SingleList_AUC.add(Double.valueOf(list.get(auc_col)));
						
					}
				}		
			}
			try {
				saveEachProjectCSV("null", datasetName, MasterList_AUC, SingleList_AUC); 
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error writing average to csv");
			}
			
			
		}
		
	}
	
	public void run(String baselinePath) {

		String[] dataset = { "camel", "eagle", "groovy", "jena", "juddi", "metamodel", "nutch" };
//		String[] dataset = { "camel", "eagle", "flink", "groovy", "jena", "juddi", "metamodel", "nutch" };
		int precision_col = 1;
		int recall_col = 2;
		int fmeasure_col = 3;
		int auc_col = 4;
		int type_col = 5;
		int dataname_col = 6;

		List<List<String>> allData = readCSV(path + baselinePath); // baselinePath is "SingleNGLPvsBaseline_result.csv"

		for (String datasetName : dataset) {
			ArrayList<Double> OriginList_AUC = new ArrayList<Double>();
			ArrayList<Double> OriginList_FMeasure = new ArrayList<Double>();
			ArrayList<Double> OriginList_Precision = new ArrayList<Double>();
			ArrayList<Double> OriginList_Recall = new ArrayList<Double>();
			ArrayList<Double> NGLPList_AUC = new ArrayList<Double>();
			ArrayList<Double> NGLPList_FMeasure = new ArrayList<Double>();
			ArrayList<Double> NGLPList_Precision = new ArrayList<Double>();
			ArrayList<Double> NGLPList_Recall = new ArrayList<Double>();
			for (List<String> newLine : allData) {
				List<String> list = newLine;
				String dataset_name = list.get(dataname_col);
				if (dataset_name.contains(datasetName)){
					if (dataset_name.contains("NGLP")) {
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else NGLPList_AUC.add(Double.valueOf(list.get(auc_col)));
						// Precision
						if (list.get(precision_col).equals("NaN")) continue;
						else NGLPList_Precision.add(Double.valueOf(list.get(precision_col)));
						// Recall
						if (list.get(recall_col).equals("NaN")) continue;
						else NGLPList_Recall.add(Double.valueOf(list.get(recall_col)));
						// F-Measure
						if (list.get(fmeasure_col).equals("NaN")) continue;
						else NGLPList_FMeasure.add(Double.valueOf(list.get(fmeasure_col)));
					}
					else { // original metric dataset
						// AUC
						if (list.get(auc_col).equals("NaN")) continue;
						else OriginList_AUC.add(Double.valueOf(list.get(auc_col)));
						// Precision
						if (list.get(precision_col).equals("NaN")) continue;
						else OriginList_Precision.add(Double.valueOf(list.get(precision_col)));
						// Recall
						if (list.get(recall_col).equals("NaN")) continue;
						else OriginList_Recall.add(Double.valueOf(list.get(recall_col)));
						// F-Measure
						if (list.get(fmeasure_col).equals("NaN")) continue;
						else OriginList_FMeasure.add(Double.valueOf(list.get(fmeasure_col)));
					}
				
				}
						
			}
			try {
				// for statistical test based on average value
				 saveAverageCSV(baselinePath, datasetName, averageArray(OriginList_AUC), averageArray(OriginList_Precision), averageArray(OriginList_Recall), averageArray(OriginList_FMeasure), averageArray(NGLPList_AUC), averageArray(NGLPList_Precision), averageArray(NGLPList_Recall), averageArray(NGLPList_FMeasure));
				// for statistical test based on each project 
//				saveEachProjectCSV(baselinePath, datasetName, NGLPList_AUC, OriginList_AUC); // baselinePath is "SingleNGLPvsBaseline_result.csv"
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error writing average to csv");
			}

		}

	}
	
	public static void saveEachProjectCSV(String baselinePath, String dataset, ArrayList<Double> NGLPList_AUC, ArrayList<Double> OriginList_AUC) throws Exception {
//		String splitDelimeterFirstElement = baselinePath.split("_")[0]; // Maybe splitDelimeterFirstElement is SingleNGLPvsBaseline.
//		System.out.println("splitDelimeterFirstElement : " + splitDelimeterFirstElement);
//		FileWriter writer = new FileWriter(path + splitDelimeterFirstElement + "_" + dataset + "_project.csv", true);
		FileWriter writer = new FileWriter(path + "MasterNGLPvsSingleNGLP" + "_" + dataset + "_project.csv", true);
		
		for(int i = 0; i < NGLPList_AUC.size(); i++) {
			if(i == 0) {
				ArrayList<String> baselineList = new ArrayList<String>();
				baselineList.add("MasterNGLP_AUC"); // change per comparing model
				baselineList.add("SingleNGLP_AUC"); // change per comparing model
				CSVUtils.writeLine(writer, baselineList);
			}
			CSVUtils.writeLine(writer, Arrays.asList(String.valueOf(NGLPList_AUC.get(i)), String.valueOf(OriginList_AUC.get(i))));
			
		}
		
		writer.flush();
		writer.close();
	}
	
	public static void saveAverageCSV(String baselinePath, String dataset, Double OriginList_AUC_Average, Double OriginList_Precision_Average, Double OriginList_Recall_Average, Double OriginList_FMeasure_Average, Double NGLPList_AUC_Average, Double NGLPList_Precision_Average, Double NGLPList_Recall_Average,
			Double NGLPList_FMeasure_Average) throws Exception {
		FileWriter writer = new FileWriter(path + baselinePath + "_average.csv", true);
		// Add header for R studio when the first data set
		if (dataset.equals("camel")) {
			ArrayList<String> baselineList = new ArrayList<String>();
			baselineList.add("");
			baselineList.add("Origin_AUC");
			baselineList.add("Origin_Precision");
			baselineList.add("Origin_Recall");
			baselineList.add("Origin_FMeasure");
			baselineList.add("NGLPList_AUC");
			baselineList.add("NGLPList_Precision");
			baselineList.add("NGLPList_Recall");
			baselineList.add("NGLPList_FMeasure");
			CSVUtils.writeLine(writer, baselineList);
		}
		CSVUtils.writeLine(writer,
				Arrays.asList(dataset, String.valueOf(OriginList_AUC_Average), String.valueOf(OriginList_Precision_Average), String.valueOf(OriginList_Recall_Average), String.valueOf(OriginList_FMeasure_Average), String.valueOf(NGLPList_AUC_Average), String.valueOf(NGLPList_Precision_Average),
						String.valueOf(NGLPList_Recall_Average), String.valueOf(NGLPList_FMeasure_Average)));
		writer.flush();
		writer.close();
	}

	public double averageArray(ArrayList<Double> list) {
		// if(list.size() == 0) {
		// System.out.println("Can not average list because list size is 0, check your
		// list!");
		// System.exit(-1);
		// }
		double sum = 0;
		double average = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		average = sum / list.size();
		return average;
	}

	public static List<List<String>> readCSV(String path) {
		// 반환용 리스트
		List<List<String>> ret = new ArrayList<List<String>>();
		BufferedReader br = null;

		try {
			br = Files.newBufferedReader(Paths.get(path));
			String line = "";
			while ((line = br.readLine()) != null) {
				// CSV 1행을 저장하는 리스트
				List<String> tmpList = new ArrayList<String>();

				String array[] = line.split(",");

				// 배열에서 리스트 반환
				tmpList = Arrays.asList(array);
				// System.out.println(tmpList);
				ret.add(tmpList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
