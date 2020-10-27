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

public class FindStatisticallyMeaningfulResults {
	// CFS vs None
	static String WilcoxonRootPath = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_analysis/CFS/Wicoxon/";
	static String CliffRootPath = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_analysis/CFS/Cliff/";
	
	public static void main(String[] args) throws IOException {
		String sourceCSV = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_analysis/CFS/CFS_None_NoPT.csv";
		
		BufferedWriter destBufWriter = Files.newBufferedWriter(Paths.get("/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_analysis/CFS/Statistical_CFS_None_NoPT.csv"));
		
		List<List<String>> allData = readCSV(sourceCSV);
		int outterForStateCnt = 0;
		for (List<String> rowLine : allData) {
			outterForStateCnt++;
			if(outterForStateCnt < 2) continue;
			
			String mlName = rowLine.get(0);
			String measureName = rowLine.get(1);
			String version = rowLine.get(2);
			String NoneApproachValue = rowLine.get(3);
			int innerForStateCnt = 0;
			
			String measureNameAddedNum = "";
			for (String element : rowLine) {
				innerForStateCnt++;
				if(innerForStateCnt < 5) continue; // 네번째 포문까지는 통
			
		    		if(measureName.equals("AUC")) measureNameAddedNum = "_1_AUC";
		    		else if(measureName.equals("Precision")) measureNameAddedNum = "_2_Precision";
		    		else if(measureName.equals("Recall")) measureNameAddedNum = "_3_Recall";
		    		else if(measureName.equals("Fmeasure")) measureNameAddedNum = "_4_Fmeasure";
		    		else if(measureName.equals("MCC")) measureNameAddedNum = "_5_MCC";
		    		
		    		String WilcoxonPath = "";
		    		String pValueString = "";
		    		String CliffPath = "";
		    		String cliffDeltaEffectSizeString = "";
		    		
		 
		    		
				if(version.equals("A")) {
					WilcoxonPath = WilcoxonRootPath + "A_CFS_" + mlName + "_result" + measureNameAddedNum + "_1_average_Wilcoxon_signed_rank_test_None_vs_CFS.txt";
					pValueString = getWilcoxPValue(WilcoxonPath);
//					System.out.println("pValueString : " + pValueString); 
					
					CliffPath = CliffRootPath + "A_CFS_" + mlName + "_result" + measureNameAddedNum + "_1_average_Cliff_delta_test_None_vs_CFS.txt";
					cliffDeltaEffectSizeString = getCliffEffectSize(CliffPath);
					
				}
				
				else if(version.equals("B")) {
					WilcoxonPath = WilcoxonRootPath + "B_CFS_" + mlName + measureNameAddedNum + "_CFS_NoPT_6_average_Wilcoxon_signed_rank_test_None_vs_CFS.txt"; 
					pValueString = getWilcoxPValue(WilcoxonPath);
					
					CliffPath = CliffRootPath + "B_CFS_" + mlName + measureNameAddedNum + "_CFS_NoPT_6_average_Cliff_delta_test_None_vs_CFS.txt";
					cliffDeltaEffectSizeString = getCliffEffectSize(CliffPath);

				}
				// wilcox 
				BigDecimal a = new BigDecimal(pValueString);
//				System.out.println(a);
				DecimalFormat formatter = new DecimalFormat("0.0000");
//				System.out.println(formatter.format(a));
				Double pValueDouble = Double.parseDouble(formatter.format(a)); 
				if(pValueDouble < 0.05) {
//					System.out.println(pValueDouble);
					if(Double.valueOf(element) > Double.valueOf(NoneApproachValue))
						element = element + "^";
					else 
						element = element + "*";
				}
				
				// add cliff effect to the element
				element = element + cliffDeltaEffectSizeString;
						
						
				// write new csv file
				destBufWriter.write(element);
				destBufWriter.write(",");
			}
			destBufWriter.newLine();
		}
		destBufWriter.close();

	}
	
	public static String getWilcoxPValue(String srcPath) throws IOException {
//		System.out.println(srcPath);
		String pValueLine = Files.readAllLines(Paths.get(srcPath)).get(5-1);

		String arr[] = pValueLine.split("=");
		String pValueString = arr[2].trim();
		
//		System.out.println(pValueString);
		return pValueString;
	}

	public static String getCliffEffectSize(String srcPath) throws IOException {
		String cliffDeltaLine = Files.readAllLines(Paths.get(srcPath)).get(4-1);
		
//		System.out.println(cliffDeltaLine);
		String arr[] = cliffDeltaLine.split(" ");
//		System.out.println(arr[3]);
		String cliffDeltaEffectSizeString = arr[3].trim();
		
		return cliffDeltaEffectSizeString;
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