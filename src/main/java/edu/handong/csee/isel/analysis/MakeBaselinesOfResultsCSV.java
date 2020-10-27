package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.handong.csee.isel.weka.CSVUtils;

public class MakeBaselinesOfResultsCSV {

	static String[] MLmodels = {
			"LR",
			"DT",			
			"RF",
//			"NB",
//			"BN",
//			"LMT"
	};
	
	static String[] measurements = {
//			"AUC",
			"Fmeasure",
			"MCC"
	};
	
	static String dest_path = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_analysis/Multisearch_Eval/";
	static String source_path = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_results/Multisearch_Eval/";	
	

	
	public static void main(String[] args) throws IOException {
		MakeBaselinesOfResultsCSV myMBORC = new MakeBaselinesOfResultsCSV();
		String measurementName = "";
		
		for(String measure : measurements) {
			for(String ML : MLmodels) {
				for(int i = 1; i <= 5; i++) {
					if (i == 1)
						measurementName = "_1_AUC";
					else if (i == 2)
						measurementName = "_2_Precision";
					else if (i == 3)
						measurementName = "_3_Recall";
					else if (i == 4)
						measurementName = "_4_Fmeasure";
					else if (i == 5)
						measurementName = "_5_MCC";
					
					myMBORC.run(ML, measurementName, measure);


				}
			}
		}

		

	}
	
	public void run(String ML, String measurementName, String multisearchEval) throws IOException {
        BufferedWriter destBufWriter = null;
        ArrayList<Double> datasetAverageList = new ArrayList<Double>();
 
    		destBufWriter = new BufferedWriter(new FileWriter(dest_path + multisearchEval + "_Master_Thesis_Experiments_Results.csv", true));
    		
    		PrintWriter pw = new PrintWriter(destBufWriter, true);
    		
        String vASourceDataPath = source_path + multisearchEval + "_MultiSearch_" + ML + "_total_results" + measurementName + "_4_project_average.csv"; // original data
        String vBSourceDataPath = source_path + multisearchEval + "_MultiSearch_" + ML + measurementName + "_5_multicollinearity_with_None_thres_10.0_9_project_average.csv"; // with multicollinearity
        
        
        List<String> vASourceAverageList = readAveageRow(vASourceDataPath);
        List<String> vBSourceAverageList = readAveageRow(vBSourceDataPath);
        
		for(String ele : vASourceAverageList){
			pw.write(ele);
			pw.write(",");
		}
		pw.write("\n");
		
		for(String ele : vBSourceAverageList){
			pw.write(ele);
			pw.write(",");
		}
		pw.write("\n");
		
		pw.flush();
		pw.close();
		

	}

	public static List<String> readAveageRow(String rankingPath){
		List<List<String>> allData = readCSV(rankingPath);
		List<String> rowData = null;
		for(int i = 0; i < allData.size(); i++) {
			if(i == allData.size() - 1) {
				rowData = allData.get(i);
			}
			else continue;
		}
		
		return rowData;
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