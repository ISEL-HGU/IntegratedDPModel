package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SortingRankingAverage {
	static String rootPath = "/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_results/Multicollinearty_With_None_thres_10/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result_ranking_average",
//			"DT_total_results_average_meanAUC",
			"NB_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"NB_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"RF_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"DT_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LR_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"BN_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LMT_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"DT_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LMT_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LMT_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"BN_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"DT_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"BN_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"RF_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"BN_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LR_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LR_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LR_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"NB_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"NB_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"NB_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"BN_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"DT_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"DT_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"RF_1_AUC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LMT_2_Precision_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"RF_5_MCC_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LR_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"RF_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_9_project_average",
			"LMT_3_Recall_5_multicollinearity_with_None_thres_10.0_9_project_average",
//			"LR_total_results_average_meanAUC",
//			"RF_total_results_average_meanAUC",
//			"DT_total_results_ranking_average",
//			"LR_total_results_ranking_average",
//			"Logistic_noHandling_total_result_ranking_average",
//			"Add_Logistic_smote_total_result_ranking_average",
//			"Logistic_spread_total_result_ranking_average",
//			"RandomForest_noHandling_total_result_ranking_average",
//			"Add_RandomForest_smote_total_result_ranking_average"
//			"RandomForest_spread_total_result_ranking_average"
	};
	
	public static void main(String args[]) throws IOException{
		
		
		for (String filename : filenameArray) {
			Map<Double, String> hm = new HashMap<Double, String>();
			 //출력 스트림 생성
	        BufferedWriter bufWriter = null;
	        try{
	            bufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_analysis/Multicollinearty_With_None_thres_10/" + filename + "_sorting.txt"));
	            List<List<String>> allData = readCSV(rootPath + filename + ".csv");
	            // 46 line shows the value of ranking average due to a header in csv file
	            int num_of_dataset = 45;
	            List<String> lastRow = allData.get(num_of_dataset+1); 
	            for(int i = 0; i < lastRow.size(); i++) {
	            		if(lastRow.get(i).equals("")) continue;
	            		else {
	            			hm.put(Double.valueOf(lastRow.get(i)), "baseline"+i);
	            		}
	            }
	            
		    		TreeMap<Double, String> tm = new TreeMap<Double, String>(hm);
		    		Iterator<Double> iteratorKey = tm.keySet().iterator(); // 오름차순이 기본 
		    		
		    		while(iteratorKey.hasNext()) {
		    			Double key = iteratorKey.next();
//		    			System.out.println(key + " : " + tm.get(key));
		    			bufWriter.write(tm.get(key));
		    			bufWriter.write(",");
		    			bufWriter.write(String.valueOf(key));
		    			bufWriter.newLine();
		    		}
		    	
	        }catch(FileNotFoundException e){
	            e.printStackTrace();
	        }catch(IOException e){
	            e.printStackTrace();
	        }finally{
	            try{
	                if(bufWriter != null){
	                    bufWriter.close();
	                }
	            }catch(IOException e){
	                e.printStackTrace();
	            }
	        }
	        System.out.println(filename + " is complete" + "\n");
	        
		}
		
		
		changeBaselineName();
      
   }
	
   public static void changeBaselineName() throws IOException {
	   for (String filename : filenameArray) {
		   BufferedWriter bufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_analysis/Multicollinearty_With_None_thres_10/" + filename + "_sorting_approach.txt"));
		   List<List<String>> allData = readCSV("/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_analysis/Multicollinearty_With_None_thres_10/" + filename + "_sorting.txt");
           for(List<String> NewLine : allData){
	        		for(String List : NewLine) {
	        			if(List.equals("baseline1"))
	        				List = List.replace("baseline1", "None");
	        			else if(List.equals("baseline2"))
	        				List = List.replace("baseline2", "Default-PCA");	
	        			else if(List.equals("baseline3"))
		        			List = List.replace("baseline3", "NSVIF10");
	        			else if(List.equals("baseline4"))
		        			List = List.replace("baseline4", "NSVIF5");
	        			else if(List.equals("baseline5"))
		        			List = List.replace("baseline5", "NSVIF4");
	        			else if(List.equals("baseline6"))
		        			List = List.replace("baseline6", "NSVIF2.5");
	        			else if(List.equals("baseline7"))
		        			List = List.replace("baseline7", "SVIF10");
	        			else if(List.equals("baseline8"))
		        			List = List.replace("baseline8", "SVIF5");
	        			else if(List.equals("baseline9"))
		        			List = List.replace("baseline9", "SVIF4");
	        			else if(List.equals("baseline10"))
		        			List = List.replace("baseline10", "SVIF2.5");
	        			else if(List.equals("baseline11"))
		        			List = List.replace("baseline11", "VCRR");
	        				
        				bufWriter.write(List);
	        			bufWriter.write(",");
	        		}
	        		bufWriter.newLine();
	        }
           bufWriter.close();
	   }
	   
   }
   
   
   public static List<List<String>> readCSV(String file_path){
       List<List<String>> ret = new ArrayList<List<String>>();
       BufferedReader br = null;
       try{
           br = Files.newBufferedReader(Paths.get(file_path));
           String line = "";
           while((line = br.readLine()) != null){
               List<String> tmpList = new ArrayList<String>();
               String array[] = line.split(",");
               tmpList = Arrays.asList(array);
               ret.add(tmpList);
           }
       }catch(FileNotFoundException e){
           e.printStackTrace();
       }catch(IOException e){
           e.printStackTrace();
       }finally{
           try{
               if(br != null){
                   br.close();
               }
           }catch(IOException e){
               e.printStackTrace();
           }
       }
       return ret;
   }
	

}
