package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountMulticollinearity {
	
	static String path = "/Users/eunjiwon/Desktop/exp_results/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result",
//			"LR_total_results.csv",
			"NB_total_results.csv",
//			"Logistic_noHandling_total_result",
//			"Add_Logistic_smote_total_result",
//			"Logistic_spread_total_result",
//			"RandomForest_noHandling_total_result",
//			"Add_RandomForest_smote_total_result"
//			"RandomForest_spread_total_result"	
	};

	public static void main(String[] args) throws IOException {
		double T2YesCount = 0;
		double T2NoCount = 0;
		double T2ratio = 0;
		double T3YesCount = 0;
		double T3NoCount = 0;
		double T3ratio = 0;
		for(String filename : filenameArray) {
			List<List<String>> allData = readText(path + "0726_exp_results/" + filename);
			for(List<String> newLine : allData){
                List<String> list = newLine;
                if(list.get(5).equals("2") && list.get(7).equals("N")) T2NoCount++;
                if(list.get(5).equals("2") && list.get(7).equals("Y")) T2YesCount++;
                if(list.get(5).equals("3") && list.get(7).equals("N")) T3NoCount++;
                if(list.get(5).equals("3") && list.get(7).equals("Y")) T3YesCount++;
			}
			
		}
		FileWriter fw = new FileWriter(path + "0726_exp_analysis/" + "count_multicollinearity.txt", true);
		BufferedWriter bufWriter = new BufferedWriter(fw);
		T2ratio = T2YesCount / ( T2YesCount + T2NoCount);
		T3ratio = T3YesCount / ( T3YesCount + T3NoCount);
		bufWriter.newLine();
		if (T2YesCount != 0) {
			bufWriter.write("CFS multicollinearity ratio : " + T2ratio);
		}
		else {
			bufWriter.write("CFS multicollinearity ratio : 0" );
		}
		bufWriter.newLine();
		if (T3YesCount != 0) {
			bufWriter.write("WFS multicollinearity ratio : " + T3ratio);
		}
		else {
			bufWriter.write("WFS multicollinearity ratio : 0" );
		}
		bufWriter.close();
		fw.close();
		
//		System.out.println(T2YesCount +  "  " + T2NoCount);
//		T3ratio = T3YesCount / ( T3YesCount + T3NoCount);
//		System.out.println(T3ratio);
		
	
	}

	
    public static List<List<String>> readText(String file_path){
        //반환용 리스트
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        
        try{
            br = Files.newBufferedReader(Paths.get(file_path));
            String line = "";
            while((line = br.readLine()) != null){
                //CSV 1행을 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                
                String array[] = line.split(","); //
                for(int i=0; i< array.length ;i++){
                		array[i] = array[i].trim();
                }
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
