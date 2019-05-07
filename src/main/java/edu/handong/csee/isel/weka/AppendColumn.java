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
		
        //출력 스트림 생성
        BufferedWriter bufWriter = null;
        try{
            bufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/smote_total_baseline.csv"));
            //csv파일 읽기
            List<List<String>> allData = readCSV();
            
            for(List<String> newLine : allData){
                List<String> list = newLine;
                for(String data : list){
                    bufWriter.write(data);
                    bufWriter.write(",");
                }
//                System.out.println(list.get(5) + " / " + (list.get(5).equals("1")) +  " ..../ " + list.get(6) + " / " + list.get(6).contains(pca));
                if(list.get(5).equals("2")) baseline = "baseline11";
                else if(list.get(5).equals("1") && list.get(6).contains(pca)) baseline = "baseline2";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_10)) baseline = "baseline3";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_5)) baseline = "baseline4";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_4)) baseline = "baseline5";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_non_2_5)) baseline = "baseline6";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_10)) baseline = "baseline7";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_5)) baseline = "baseline8";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_4)) baseline = "baseline9";
                else if(list.get(5).equals("1") && list.get(6).contains(vif_2_5)) baseline = "baseline10";
                else baseline = "baseline1";
                 
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
    }
    
    public static List<List<String>> readCSV(){
        //반환용 리스트
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        
        try{
            br = Files.newBufferedReader(Paths.get("/Users/eunjiwon/Desktop/smote_total_result.csv"));
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
