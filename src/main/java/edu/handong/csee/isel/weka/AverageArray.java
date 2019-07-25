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



public class AverageArray {
	int numberOfApproaches = 13;
	static String path = "/Users/eunjiwon/Desktop/0723_csv_results/";
	static String[] filenameArray = {
//			"DecisionTree_noHadling_total_result",
			"Add_DecisionTree_smote_total_result",
//			"DecisionTree_spread_total_result",
//			"Logistic_noHandling_total_result",
			"Add_Logistic_smote_total_result",
//			"Logistic_spread_total_result",
//			"RandomForest_noHandling_total_result",
			"Add_RandomForest_smote_total_result"
//			"RandomForest_spread_total_result"
	};
	

	public static void main(String[] args){
		AverageArray myAverageArray = new AverageArray();
		for(String filename : filenameArray) {
//			myAverageArray.run(filename);
//			myAverageArray.saveRankingCSV(filename);
			myAverageArray.saveRankingAverageCSV(filename);
		}

	}
	
	
	public void saveRankingAverageCSV(String baselinePath) {
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
		ArrayList<Double> b12List = new ArrayList<Double>();
		ArrayList<Double> b13List = new ArrayList<Double>();
        BufferedWriter bufWriter = null;

        try{
            bufWriter = Files.newBufferedWriter(Paths.get(path + baselinePath + "_ranking_average.csv"));

            List<List<String>> allData = readCSV(path + baselinePath + "_ranking.csv");
            
            for(List<String> newLine : allData){
                List<String> list = newLine;
                for(int i = 1; i < list.size(); i++) {
                		if(i == 1) b1List.add(Double.valueOf(list.get(i)));
                		if(i == 2) b2List.add(Double.valueOf(list.get(i))); 
                		if(i == 3) b3List.add(Double.valueOf(list.get(i)));
                		if(i == 4) b4List.add(Double.valueOf(list.get(i)));
                		if(i == 5) b5List.add(Double.valueOf(list.get(i)));
                		if(i == 6) b6List.add(Double.valueOf(list.get(i))); 
                		if(i == 7) b7List.add(Double.valueOf(list.get(i)));
                		if(i == 8) b8List.add(Double.valueOf(list.get(i))); 
                		if(i == 9) b9List.add(Double.valueOf(list.get(i)));
                		if(i == 10) b10List.add(Double.valueOf(list.get(i))); 
                		if(i == 11) b11List.add(Double.valueOf(list.get(i)));
                		if(i == 12) b12List.add(Double.valueOf(list.get(i)));
                		if(i == 13) b13List.add(Double.valueOf(list.get(i)));
                }
            }
            
            for(int i = 0; i < numberOfApproaches + 1; i++) {
            		if(i == 0) {
            			bufWriter.write(",");
            		}
            		else {
            			bufWriter.write("baseline" + i);
                		bufWriter.write(",");
            		}
            }
            bufWriter.newLine();
            
            for(List<String> NewLine : allData){
	        		for(String List : NewLine) {
	        			bufWriter.write(List);
	        			bufWriter.write(",");
	        		}
	        		bufWriter.newLine();
	        }
            
	        for(int i = 0; i < numberOfApproaches + 1; i++) {
	        		if(i == 1) bufWriter.write(String.valueOf(averageArray(b1List)));
	        		if(i == 2) bufWriter.write(String.valueOf(averageArray(b2List)));
	        		if(i == 3) bufWriter.write(String.valueOf(averageArray(b3List)));
	        		if(i == 4) bufWriter.write(String.valueOf(averageArray(b4List)));
	        		if(i == 5) bufWriter.write(String.valueOf(averageArray(b5List)));
	        		if(i == 6) bufWriter.write(String.valueOf(averageArray(b6List)));
	        		if(i == 7) bufWriter.write(String.valueOf(averageArray(b7List)));
	        		if(i == 8) bufWriter.write(String.valueOf(averageArray(b8List)));
	        		if(i == 9) bufWriter.write(String.valueOf(averageArray(b9List)));
	        		if(i == 10) bufWriter.write(String.valueOf(averageArray(b10List))); 
	        		if(i == 11) bufWriter.write(String.valueOf(averageArray(b11List)));
	        		if(i == 12) bufWriter.write(String.valueOf(averageArray(b12List))); 
	        		if(i == 13) bufWriter.write(String.valueOf(averageArray(b13List)));
	    			bufWriter.write(",");
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
	
	public void saveRankingCSV(String baselinePath) {
	    //출력 스트림 생성
        BufferedWriter bufWriter = null;
        ArrayList<Double> datasetAverageList = new ArrayList<Double>();
        try{
            bufWriter = Files.newBufferedWriter(Paths.get(path + baselinePath + "_ranking.csv"));
            //csv파일 읽기
            List<List<String>> allData = readCSV(path + baselinePath + "_average.csv");
            
            for(List<String> newLine : allData){
            		if(newLine.contains("None")) continue; // average 파일에 헤더 추가해서 
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
		int[] ranking = new int[numberOfApproaches];
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
	
    public void run(String baselinePath) {
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
    		ArrayList<Double> b12List = new ArrayList<Double>(); // LR
    		ArrayList<Double> b13List = new ArrayList<Double>(); // NB
    		String[] dataset = {"AEEEM_EQ", "AEEEM_JDT", "AEEEM_LC", "AEEEM_ML", "AEEEM_PDE", "JIT_bugzilla", "JIT_columba", "JIT_jdt", "JIT_mozilla", "JIT_platform", "JIT_postgres", "Relink_Apache", "Relink_Safe", "Relink_Zxing"};	

        // f-measure is a column 3, AUC is a column 4, targetPath is a column 6, baselineType is a column 7 in "/Users/eunjiwon/Desktop/spreadsubsampling_total_baseline.csv".
    		List<List<String>> allData = readCSV(path + baselinePath + "_baseline.csv");
    		
//        List<List<String>> allData = readCSV("/Users/eunjiwon/Desktop/exp_results/smote_total_baseline.csv");
//        try {
//			saveAverageCSV("Dataset Name", Double.valueOf("1"), Double.valueOf("2"), Double.valueOf("3"), Double.valueOf("4"), Double.valueOf("5"), Double.valueOf("6"), Double.valueOf("7"), Double.valueOf("8"), Double.valueOf("9"), Double.valueOf("10"), Double.valueOf("11"));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    		
        for(String datasetName : dataset) {
            for(List<String> newLine : allData) {
                List<String> list = newLine;
                    if(list.get(6).contains(datasetName) && list.get(7).equals("None")) {
                    		if(list.get(4).equals("NaN")) continue;
                    		else b1List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("Default-PCA")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b2List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("NSVIF10")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b3List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("NSVIF5")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b4List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("NSVIF4")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b5List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("NSVIF2.5")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b6List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("SVIF10")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b7List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("SVIF5")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b8List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("SVIF4")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b9List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("SVIF2.5")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b10List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("CFS-BestFirst")) {
	                		if(list.get(4).equals("NaN")) continue;
	                		else b11List.add(Double.valueOf(list.get(4)));
                    }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("LR+AUC")) {
                		if(list.get(4).equals("NaN")) continue;
                		else b12List.add(Double.valueOf(list.get(4)));
                }
                    else if(list.get(6).contains(datasetName) && list.get(7).equals("NB+AUC")) {
                		if(list.get(4).equals("NaN")) continue;
                		else b13List.add(Double.valueOf(list.get(4)));
                }
                    
            }
            // 여기에 플래그를 둬서 처음에 쓸 때만 위에 헤더 달게 할까? 그거 해야지 R로 프리드만 진행할 수 있음!
            try {
            		saveAverageCSV(baselinePath, datasetName, averageArray(b1List), averageArray(b2List), averageArray(b3List), averageArray(b4List), averageArray(b5List), averageArray(b6List), averageArray(b7List), averageArray(b8List), averageArray(b9List), averageArray(b10List), averageArray(b11List), averageArray(b12List), averageArray(b13List));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error writing average to csv");
			}
            
        }
 
    }
    
	public static void saveAverageCSV(String baselinePath, String dataset, Double b1Average, Double b2Average, Double b3Average, Double b4Average, Double b5Average, Double b6Average, Double b7Average, Double b8Average, Double b9Average, Double b10Average, Double b11Average, Double b12Average, Double b13Average) throws Exception {
//		String csvFile = "/Users/eunjiwon/Desktop/exp_results/smote_AUC_average.csv";
		FileWriter writer =  new FileWriter(path + baselinePath + "_average.csv", true);

		if(dataset.equals("AEEEM_EQ")) {
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
			baselineList.add("CFS-BestFirst");
			baselineList.add("LR+AUC");
			baselineList.add("NB+AUC");
			CSVUtils.writeLine(writer, baselineList);
		}
		CSVUtils.writeLine(writer, Arrays.asList(dataset, String.valueOf(b1Average), String.valueOf(b2Average), String.valueOf(b3Average), String.valueOf(b4Average), String.valueOf(b5Average), String.valueOf(b6Average), String.valueOf(b7Average), String.valueOf(b8Average), String.valueOf(b9Average), String.valueOf(b10Average), String.valueOf(b11Average), String.valueOf(b12Average), String.valueOf(b13Average)));
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
