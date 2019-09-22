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

import edu.handong.csee.isel.weka.CSVUtils;


public class AppendColumn {
	static String path = "/Users/eunjiwon/Desktop/exp_results/0726_exp_results/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result",
//			"LR_total_results",
			"DT_compare",
//			"Logistic_noHandling_total_result",
//			"Add_Logistic_smote_total_result",
//			"Logistic_spread_total_result",
//			"RandomForest_noHandling_total_result",
//			"Add_RandomForest_smote_total_result"
//			"RandomForest_spread_total_result"	
	};
	
	static void changeFormatForCliffDelta() throws IOException {
		for (String filename : filenameArray) {
			 //출력 스트림 생성
	        
	        		FileWriter writer =  new FileWriter(path + filename + "_2_5.csv", true);    
	            //csv파일 읽기
	            List<List<String>> allData = readCSV(path + filename + ".csv");
		        	ArrayList<String> b1List = new ArrayList<String>();
		    		ArrayList<String> b2List = new ArrayList<String>();
		    		ArrayList<String> b3List = new ArrayList<String>();
		    		ArrayList<String> b4List = new ArrayList<String>();
		    		ArrayList<String> b5List = new ArrayList<String>();
		    		ArrayList<String> b6List = new ArrayList<String>();
		    		ArrayList<String> b7List = new ArrayList<String>();
		    		ArrayList<String> b8List = new ArrayList<String>();
		    		ArrayList<String> b9List = new ArrayList<String>();
		    		ArrayList<String> b10List = new ArrayList<String>();
		    		ArrayList<String> b11List = new ArrayList<String>();
		    		ArrayList<String> b12List = new ArrayList<String>();
		    		for(List<String> newLine : allData){
		    			List<String> list = newLine;
		    			if(list.get(3).equals("2.5")) {
		    				if(list.get(5).equals("None")) b1List.add(list.get(4));
			    			else if(list.get(5).equals("Default-PCA")) b2List.add( list.get(4));
			    			else if(list.get(5).equals("NSVIF10")) b3List.add( list.get(4));
			    			else if(list.get(5).equals("NSVIF5")) b4List.add( list.get(4));
			    			else if(list.get(5).equals("NSVIF4")) b5List.add( list.get(4));
			    			else if(list.get(5).equals("NSVIF2.5")) b6List.add( list.get(4));
			    			else if(list.get(5).equals("SVIF10")) b7List.add( list.get(4));
			    			else if(list.get(5).equals("SVIF5")) b8List.add( list.get(4));
			    			else if(list.get(5).equals("SVIF4")) b9List.add( list.get(4));
			    			else if(list.get(5).equals("SVIF2.5")) b10List.add( list.get(4));
			    			else if(list.get(5).equals("CFS-BestFirst")) b11List.add( list.get(4));
			    			else if(list.get(5).equals("WFS-BestFirst")) b12List.add( list.get(4));
		    			}
		    			
		    		}
		    		ArrayList<String> baselineList = new ArrayList<String>();
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
	    			baselineList.add("CFS-BestFirst");
	    			baselineList.add("WFS-BestFirst");
	    			CSVUtils.writeLine(writer, baselineList);
	            for(int i = 0; i < b1List.size(); i++) {
		            	CSVUtils.writeLine(writer, Arrays.asList(b1List.get(i), b2List.get(i), b3List.get(i), b4List.get(i), b5List.get(i), b6List.get(i), b7List.get(i), b8List.get(i), b9List.get(i), b10List.get(i), b11List.get(i), b12List.get(i)));
		        		
	            }
	            writer.flush();
        			writer.close();
	            System.out.println(filename + " is complete");
		}
      
	}
	
	
	public static void main(String[] args) throws IOException{
		changeFormatForCliffDelta();
		
//		String pca = "_PCA";
//		String vif_non_10 = "_NONSTEPWISE_10";
//		String vif_non_5 = "_NONSTEPWISE_5";
//		String vif_non_4 = "_NONSTEPWISE_4";
//		String vif_non_2_5 = "_NONSTEPWISE_2_5";
//		String vif_10 = "_STEPWISE_10";
//		String vif_5 = "_STEPWISE_5";
//		String vif_4 = "_STEPWISE_4";
//		String vif_2_5 = "_STEPWISE_2_5";
//		String baseline = "";
//		
//
//		for (String filename : filenameArray) {
//			 //출력 스트림 생성
//	        BufferedWriter bufWriter = null;
//	        try{
//	            bufWriter = Files.newBufferedWriter(Paths.get(path + filename + "_baseline.csv"));
//	            //csv파일 읽기
//	            List<List<String>> allData = readCSV(path + filename + ".csv");
////	            int type_col = 5;
////	            int dataname_col = 6;
//	            int type_col = 2;
//	            int dataname_col = 3;
//	            for(List<String> newLine : allData){
//	                List<String> list = newLine;
//	                for(String data : list){
//	                    bufWriter.write(data);
//	                    bufWriter.write(",");
//	                }
//	                if(list.get(type_col).equals("2")) baseline = "CFS-BestFirst";
//	                else if(list.get(type_col).equals("3")) baseline = "WFS-BestFirst";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(pca)) baseline = "Default-PCA";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_non_10)) baseline = "NSVIF10";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_non_5)) baseline = "NSVIF5";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_non_4)) baseline = "NSVIF4";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_non_2_5)) baseline = "NSVIF2.5";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_10)) baseline = "SVIF10";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_5)) baseline = "SVIF5";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_4)) baseline = "SVIF4";
//	                else if(list.get(type_col).equals("1") && list.get(dataname_col).contains(vif_2_5)) baseline = "SVIF2.5";
//	                else baseline = "None";
//	                 
//	                //추가하기
//	                bufWriter.write(baseline);
//	                //개행코드추가
//	                bufWriter.newLine();
//	            }
//	        }catch(FileNotFoundException e){
//	            e.printStackTrace();
//	        }catch(IOException e){
//	            e.printStackTrace();
//	        }finally{
//	            try{
//	                if(bufWriter != null){
//	                    bufWriter.close();
//	                }
//	            }catch(IOException e){
//	                e.printStackTrace();
//	            }
//	        }
//	        System.out.println(filename + " is complete");
//		}
       
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
