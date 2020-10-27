package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AverageRanking {

	static String rankingFilesRootPath = "/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_results/";
	

	public static void main(String[] args) throws IOException {
		String sourceCSV = "/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_analysis/ICSE'21 Experiments Results.csv";
		BufferedWriter destBufWriter = Files
				.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/Multicollinearity/exp_results/ICSE21_exp_analysis/Rankings_ICSE'21 Experiments Results.csv"));
		
		
		List<List<String>> allData = readCSV(sourceCSV);
		int outterForStateCnt = 0;
		for (List<String> rowLine : allData) {
			outterForStateCnt++;
			if(outterForStateCnt < 2) continue;
			int innerForStateCnt = 0;
			
			String mlName = rowLine.get(0);
			String measureName = rowLine.get(1);
			String version = rowLine.get(2);
			String NoneApproachValue = rowLine.get(3);

			String measureNameAddedNum = "";
			
			List<String> rankingRowList = null;
			
			for (String element : rowLine) {
				innerForStateCnt++;
				if(innerForStateCnt < 4) continue; // 세번째 포문까지는 통과 
				
		    		if(measureName.equals("AUC")) measureNameAddedNum = "_1_AUC";
		    		else if(measureName.equals("Precision")) measureNameAddedNum = "_2_Precision";
		    		else if(measureName.equals("Recall")) measureNameAddedNum = "_3_Recall";
		    		else if(measureName.equals("Fmeasure")) measureNameAddedNum = "_4_Fmeasure";
		    		else if(measureName.equals("MCC")) measureNameAddedNum = "_5_MCC";
		    		
		    		String versionPath = "";
		    		
				if(version.equals("1")) {
					versionPath = rankingFilesRootPath + mlName + "_total_results" + measureNameAddedNum + "_3_ranking_average.csv";
					
				}
				else if(version.equals("2")) {
					versionPath = rankingFilesRootPath + mlName + measureNameAddedNum + "_5_multicollinearity_with_None_thres_10.0_8_ranking_average.csv";
				}
				
				rankingRowList = readRankingRow(versionPath);
				break;
			
				
			}
			for(String ele : rankingRowList)	{
				destBufWriter.write(ele);
				destBufWriter.write(",");
			}
			destBufWriter.newLine();
		}
		destBufWriter.close();

	}
	
	public static List<String> readRankingRow(String rankingPath){
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

