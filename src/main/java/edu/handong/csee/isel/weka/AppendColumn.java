package edu.handong.csee.isel.weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppendColumn {
	static String path = "/Users/eunjiwon/Desktop/exp_results/0726_exp_results/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result",
//			"LR_total_results",
			"DT_total_results",
//			"Logistic_noHandling_total_result",
//			"Add_Logistic_smote_total_result",
//			"Logistic_spread_total_result",
//			"RandomForest_noHandling_total_result",
//			"Add_RandomForest_smote_total_result"
//			"RandomForest_spread_total_result"	
	};
	
	public static void main(String[] args){
		String pca = "_PCA";
		String vif_non_10 = "_NONSTEPWISE_10";
		String vif_non_5 = "_NONSTEPWISE_5";
		String vif_non_4 = "_NONSTEPWISE_4";
		String vif_non_2_5 = "_NONSTEPWISE_2_5";
		String vif_10 = "_STEPWISE_10";
		String vif_5 = "_STEPWISE_5";
		String vif_4 = "_STEPWISE_4";
		String vif_2_5 = "_STEPWISE_2_5";
		String baseline = "";
		

		
		for (String filename : filenameArray) {
			 //출력 스트림 생성
	        BufferedWriter bufWriter = null;
	        try{
	            bufWriter = Files.newBufferedWriter(Paths.get(path + filename + "_baseline.csv"));
	            //csv파일 읽기
	            List<List<String>> allData = readCSV(path + filename + ".csv");
	            
	            for(List<String> newLine : allData){
	                List<String> list = newLine;
	                for(String data : list){
	                    bufWriter.write(data);
	                    bufWriter.write(",");
	                }
	                if(list.get(5).equals("2")) baseline = "CFS-BestFirst";
	                else if(list.get(5).equals("3")) baseline = "WFS-BestFirst";
	                else if(list.get(5).equals("1") && list.get(6).contains(pca)) baseline = "Default-PCA";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_10)) baseline = "NSVIF10";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_5)) baseline = "NSVIF5";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_4)) baseline = "NSVIF4";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_2_5)) baseline = "NSVIF2.5";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_10)) baseline = "SVIF10";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_5)) baseline = "SVIF5";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_4)) baseline = "SVIF4";
	                else if(list.get(5).equals("1") && list.get(6).contains(vif_2_5)) baseline = "SVIF2.5";
	                else baseline = "None";
	                 
	                //추가하기
	                bufWriter.write(baseline);
	                //개행코드추가
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
	        System.out.println(filename + " is complete");
		}
       
    }
    
    public static List<List<String>> readCSV(String file_path){
        //반환용 리스트
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        
        try{
            br = Files.newBufferedReader(Paths.get(file_path));
            String line = "";
            while((line = br.readLine()) != null){
                //CSV 1행을 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                
                String array[] = line.split(",");
     
                //배열에서 리스트 반환
                tmpList = Arrays.asList(array);
//                System.out.println(tmpList);
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
