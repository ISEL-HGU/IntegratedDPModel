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
	static String rootPath = "/Users/eunjiwon/Desktop/Multicollinearity/exp_results/0229_exp_results/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result_ranking_average",
			"DT_total_results_ranking_average",
			"LR_total_results_ranking_average",
//			"Logistic_noHandling_total_result_ranking_average",
//			"Add_Logistic_smote_total_result_ranking_average",
//			"Logistic_spread_total_result_ranking_average",
//			"RandomForest_noHandling_total_result_ranking_average",
//			"Add_RandomForest_smote_total_result_ranking_average"
//			"RandomForest_spread_total_result_ranking_average"
	};
	
	public static void main(String args[]){
		for (String filename : filenameArray) {
			Map<Double, String> hm = new HashMap<Double, String>();
			 //출력 스트림 생성
	        BufferedWriter bufWriter = null;
	        try{
	            bufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/Multicollinearity/exp_results/0229_exp_analysis/" + filename + "_sorting.txt"));
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
		    			bufWriter.write(" : ");
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
