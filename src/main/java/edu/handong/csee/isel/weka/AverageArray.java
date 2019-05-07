package edu.handong.csee.isel.weka;

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


// 그 csv 파일로 랭킹 매기는 것도 따로 함수 만들어야 겠다. 
public class AverageArray {
	public static void main(String[] args){
		AverageArray myAverageArray = new AverageArray();
		myAverageArray.run();
		myAverageArray.saveRankingCSV();
	}
	public void saveRankingCSV() {
	    //출력 스트림 생성
        BufferedWriter bufWriter = null;
        ArrayList<Double> datasetAverageList = new ArrayList<Double>();
        try{
            bufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/exp_results/smote_AUC_ranking.csv"));
            //csv파일 읽기
            List<List<String>> allData = readCSV("/Users/eunjiwon/Desktop/exp_results/smote_AUC_average.csv");
            
            for(List<String> newLine : allData){
                List<String> list = newLine;
                for(int i = 1; i < list.size(); i++) {
                		datasetAverageList.add(Double.valueOf(list.get(i)));
                }
                bufWriter.write(list.get(0));
                bufWriter.write(",");
                for(int rank : ranking(datasetAverageList)){
                    bufWriter.write(String.valueOf(rank));
                    bufWriter.write(",");
                }
                bufWriter.newLine();
                datasetAverageList.clear();
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
	
	public int[] ranking(ArrayList<Double> list) {
		int[] ranking = new int[11];
		for(int i = 0; i < ranking.length; i++) {
			ranking[i] = 1;
		} 
		for(int i = 0; i < list.size(); i++){
			for (int j = 0; j < list.size(); j++) {                            
				if(list.get(i) < list.get(j)){   
					ranking[i]++;               
				}              
			}          
		}    

		return ranking;
	}
	
    public void run() {
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
    		String[] dataset = {"AEEEM_EQ", "AEEEM_JDT", "AEEEM_LC", "AEEEM_ML", "AEEEM_PDE", "JIT_bugzilla", "JIT_columba", "JIT_jdt", "JIT_mozilla", "JIT_platform", "JIT_postgres", "Relink_Apache", "Relink_Safe", "Relink_Zxing"};	

        // f-measure is a column 3, AUC is a column 4, targetPath is a column 6, baselineType is a column 7 in "/Users/eunjiwon/Desktop/spreadsubsampling_total_baseline.csv".
            
        List<List<String>> allData = readCSV("/Users/eunjiwon/Desktop/exp_results/smote_total_baseline.csv");
//        try {
//			saveAverageCSV("Dataset Name", Double.valueOf("1"), Double.valueOf("2"), Double.valueOf("3"), Double.valueOf("4"), Double.valueOf("5"), Double.valueOf("6"), Double.valueOf("7"), Double.valueOf("8"), Double.valueOf("9"), Double.valueOf("10"), Double.valueOf("11"));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        for(String datasetName : dataset) {
            for(List<String> newLine : allData) {
                List<String> list = newLine;
                    if(list.get(6).contains(datasetName) && list.get(7).equals("baseline1")) {
                    		if(list.get(4).equals("NaN")) continue;
                    		else b1List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline2")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b2List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline3")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b3List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline4")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b4List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline5")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b5List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline6")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b6List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline7")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b7List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline8")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b8List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline9")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b9List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline10")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b10List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("baseline11")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b11List.add(Double.valueOf(list.get(4)));
                    }
            }
            try {
            		saveAverageCSV(datasetName, averageArray(b1List), averageArray(b2List), averageArray(b3List), averageArray(b4List), averageArray(b5List), averageArray(b6List), averageArray(b7List), averageArray(b8List), averageArray(b9List), averageArray(b10List), averageArray(b11List));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error writing average to csv");
			}
            
        }
 
    }
    
	public static void saveAverageCSV(String dataset, Double b1Average, Double b2Average, Double b3Average, Double b4Average, Double b5Average, Double b6Average, Double b7Average, Double b8Average, Double b9Average, Double b10Average, Double b11Average) throws Exception {
		String csvFile = "/Users/eunjiwon/Desktop/exp_results/smote_AUC_average.csv";
		FileWriter writer =  new FileWriter(csvFile, true);
		CSVUtils.writeLine(writer, Arrays.asList(dataset, String.valueOf(b1Average), String.valueOf(b2Average), String.valueOf(b3Average), String.valueOf(b4Average), String.valueOf(b5Average), String.valueOf(b6Average), String.valueOf(b7Average), String.valueOf(b8Average), String.valueOf(b9Average), String.valueOf(b10Average), String.valueOf(b11Average)));
		writer.flush();
		writer.close();
	}
	
	public double averageArray(ArrayList<Double> list) {
		double sum = 0;
		double average = 0;
		for(int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		average = sum / list.size();
		return average;
	}
	
    public static List<List<String>> readCSV(String path){
        //반환용 리스트
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        
        try{
            br = Files.newBufferedReader(Paths.get(path));
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
