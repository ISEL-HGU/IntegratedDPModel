package edu.handong.csee.isel.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset.Entry;

public class NaNCounter {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		NaNCounter myNaNCounter = new NaNCounter();
		List<List<String>> allData = AverageArray.readCSV("/Users/eunjiwon/Desktop/LR_total_results.csv");
		int precisionPos = 1;
		int recallPos = 2;
		int fmeasurePos = 3;
		int aucPos = 4;
		int mccPos = 5;
		myNaNCounter.saveCSVNaNInfo(precisionPos, allData);
		myNaNCounter.saveCSVNaNInfo(recallPos, allData);
		myNaNCounter.saveCSVNaNInfo(fmeasurePos, allData);
		myNaNCounter.saveCSVNaNInfo(aucPos, allData);
		myNaNCounter.saveCSVNaNInfo(mccPos, allData);
	}
	
	public void saveCSVNaNInfo(int measurePosition, List<List<String>> allData) throws IOException {
		String measureName = "";
		int NaNCount = 0;
		
		ArrayListMultimap<String, String> NaNMap = ArrayListMultimap.create(); // single key and multiple values
		ArrayListMultimap<String, String> datasetCntMap = ArrayListMultimap.create();
		
		
		if(measurePosition == 1) measureName = "Precision";
		else if(measurePosition == 2) measureName = "Recall";
		else if(measurePosition == 3) measureName = "Fmeasure";
		else if(measurePosition == 4) measureName = "AUC";
		else if(measurePosition == 5) measureName = "MCC";
		
		String tempPerformance;
		String absoluteSourcePath;
		for(List<String> newLine : allData){
			absoluteSourcePath = newLine.get(18); // source position
			File file = new File(absoluteSourcePath); // absolute path
			String multicollinearity_sourcepath = file.getName(); // only file name
			multicollinearity_sourcepath = multicollinearity_sourcepath.replace(".arff", "");
			String[] multicollinearity_sourcepath_array;
			multicollinearity_sourcepath_array = multicollinearity_sourcepath.split("_");
			String project = multicollinearity_sourcepath_array[0];
			String dataset = multicollinearity_sourcepath_array[1];
			String dataset_name = project + "_" + dataset;
			datasetCntMap.put(dataset_name, absoluteSourcePath);
			
			tempPerformance = newLine.get(measurePosition);
			if(tempPerformance.equals("NaN")) {
				NaNCount++;
				NaNMap.put(dataset_name, absoluteSourcePath);
			}
			
		}
		
		Set<String> keySet = NaNMap.keySet();
		
        FileWriter writer = new FileWriter("/Users/eunjiwon/Desktop/LR_total_results_NaN_analysis.csv", true);
        writer.write("Measurement : " + measureName);
        writer.write("\n");
        writer.write("NaNCount : " + NaNCount + " / 49500 (" + String.format("%.2f", (Double.valueOf(NaNCount) / 49500) * 100) + "%)");
        writer.write("\n");
        
        ListMultimap<String, Integer> Sorting_map = Multimaps.newListMultimap(new TreeMap<>(), ArrayList::new);
        Set<String> keySet_Sorting_map = Sorting_map.keySet();
        
        for(String tempKey: keySet){
            Sorting_map.put(tempKey, NaNMap.get(tempKey).size());
        }
        
        List<List<String>> allData1 = AverageArray.readCSV("/Users/eunjiwon/Desktop/dataset_table.csv");
        
        writer.write("Dataset");
		writer.write(",");
		writer.write("# of All Instances");
		writer.write(",");
		writer.write("# of Buggy Instances / Ratio");
		writer.write(",");
		writer.write("# of All Predictions");
		writer.write(",");
		writer.write("# of NaN Predictions");
		writer.write(",");
		writer.write("NaN Ratio");
		writer.write("\n");
		
		
        for(List<String> NewLine : allData1){
        		String dataset_name = NewLine.get(0);
        		for(String List : NewLine) {
        			writer.write(List);
        			writer.write(",");
        		}
        		if(keySet_Sorting_map.contains(dataset_name)) {
        			
        			writer.write(datasetCntMap.get(dataset_name).size() + " ");
        			writer.write(",");
        			writer.write(Sorting_map.get(dataset_name).get(0) + " ");
        			writer.write(",");
        			writer.write(String.format("%.2f", (Double.valueOf(Sorting_map.get(dataset_name).get(0)) / datasetCntMap.get(dataset_name).size()) * 100) + "%");
        			
        		}
        		else {
        			writer.write(datasetCntMap.get(dataset_name).size() + " ");
        			writer.write(",");
        			writer.write("0");
        			writer.write(",");
        			writer.write("0%");
        		}
        		
        		
        		writer.write("\n");
        }
        
        
       
        writer.write("\n"); 
        writer.flush();
        writer.close();
      
	}
	

	public void printTxtNaNInfo(int measurePosition, List<List<String>> allData) throws IOException {
		String measureName = "";
		int NaNCount = 0;
//		Map<String, String> NaNMap = new HashMap<>();
		ArrayListMultimap<String, String> NaNMap = ArrayListMultimap.create(); // single key and multiple values

		if(measurePosition == 1) measureName = "Precision";
		else if(measurePosition == 2) measureName = "Recall";
		else if(measurePosition == 3) measureName = "Fmeasure";
		else if(measurePosition == 4) measureName = "AUC";
		else if(measurePosition == 5) measureName = "MCC";
		
		String tempPerformance;
		String absoluteSourcePath;
		for(List<String> newLine : allData){
			tempPerformance = newLine.get(measurePosition);
			if(tempPerformance.equals("NaN")) {
				NaNCount++;
				absoluteSourcePath = newLine.get(18); // source position
				File file = new File(absoluteSourcePath); // absolute path
				String multicollinearity_sourcepath = file.getName(); // only file name
				multicollinearity_sourcepath = multicollinearity_sourcepath.replace(".arff", "");
				String[] multicollinearity_sourcepath_array;
				multicollinearity_sourcepath_array = multicollinearity_sourcepath.split("_");
				String project = multicollinearity_sourcepath_array[0];
				String dataset = multicollinearity_sourcepath_array[1];
				NaNMap.put(project + "_" + dataset, absoluteSourcePath);
			}
		}
		
		Set<String> keySet = NaNMap.keySet();
  
        FileWriter writer = new FileWriter("/Users/eunjiwon/Desktop/Sorting_MCC_MultiSearch_LR_total_results_NaN_analysis.txt", true);
        writer.write("Measurement : " + measureName);
        writer.write("\n");
        writer.write("NaNCount : " + NaNCount + " / 49500 (" + String.format("%.2f", (Double.valueOf(NaNCount) / 49500) * 100) + "%)");

        writer.write("\n");
        writer.write("Dataset List : " + keySet);
        writer.write("\n");
        writer.write("# of Dataset : " + keySet.size());
        writer.write("\n");
        
        ListMultimap<Integer, String> Sorting_map = Multimaps.newListMultimap(new TreeMap<>(), ArrayList::new);
        Set<Integer> keySet_Sorting_map = Sorting_map.keySet();
        
        for(String tempKey: keySet){
            Sorting_map.put(NaNMap.get(tempKey).size(), tempKey);
        }
        
        for(Integer tempKey: keySet_Sorting_map){
            writer.write("# of NaN prediction results : " + tempKey + " --- " + Sorting_map.get(tempKey));
            writer.write("\n");
        }
        
        
        writer.write("\n"); 
        
        
        writer.flush();
        writer.close();
		
	}
}
