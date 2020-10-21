package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.handong.csee.isel.weka.CSVUtils;

public class CompareApproachesBasedOnMulticollinearity {
	static int numberOfApproaches = 11;
	int dataname_col = 18;
	int approachname_col = 19;
	static String measurementName = "";
	static String[] MLmodels = {
			"DT",			
			"LR",
			"RF",
			"NB",
			"LMT",
			"BN"
	};
	static String[] measurements = {
			"AUC",
			"Fmeasure",
			"MCC"
	};
	
	static String path = "/Users/eunjiwon/Desktop/Researches/Multicollinearity/exp_results/Master_thesis_exp_results/Multisearch_Eval/";
	
	
	public static void main(String[] args) throws IOException {
		CompareApproachesBasedOnMulticollinearity myCABOM = new CompareApproachesBasedOnMulticollinearity();
		
		// execute on the server to get "_5_multicollinearity_with_None_thres_" file
		for(String measure : measurements) {
			for (String ML : MLmodels) {
				String inputPath = "/home/eunjiwon/Git/MulticollinearityExpTool/multi_results/" + measure + "_MultiSearch_" + ML + "_total_results.csv"; 
				String outputPath = "/home/eunjiwon/Git/MulticollinearityExpTool/multi_results/" + measure + "_MultiSearch_" + ML; // None 기준으로 다중공선성 있는 것들만 따로 모아놓음
				String approachName = "None";
				
				String thresholdVIF = "10.0";
				ArrayList<String> listOfMulticollinearityData_10 = myCABOM.savedDataHavingMulticollinearity(inputPath, thresholdVIF, approachName);
				for (int i = 1; i <= 5; i++) {
					myCABOM.savedPerformanceOfApproaches(inputPath, outputPath, listOfMulticollinearityData_10, thresholdVIF, i);
				}
			}
		}

		
		// execute on the local
//		for(String ML : MLmodels) {
//			for(int i = 1; i <= 5; i++) {
//				if (i == 4)
//					measurementName = "_1_AUC";
//				else if (i == 1)
//					measurementName = "_2_Precision";
//				else if (i == 2)
//					measurementName = "_3_Recall";
//				else if (i == 3)
//					measurementName = "_4_Fmeasure";
//				else if (i == 5)
//					measurementName = "_5_MCC";
////				String filename = ML + measurementName + "_5_multicollinearity_with_None_thres_10.0"; // DT_4_Fmeasure_5_multicollinearity_with_None_thres_10.0
//				String filename = "MultiSearch_" + ML + measurementName + "_5_multicollinearity_with_None_thres_10.0"; // DT_4_Fmeasure_5_multicollinearity_with_None_thres_10.0
//				myCABOM.run(filename);
//				myCABOM.saveRankingCSV(filename);
//				myCABOM.saveRankingAverageCSV(filename);
//				myCABOM.saveProjectAverageCSV(filename);
//
//			}
//		}
		
		 
		
	}

	public void run(String baselinePath) {

		String[] dataset = { "AEEEM_EQ", "AEEEM_JDT", "AEEEM_LC", "AEEEM_ML", "AEEEM_PDE", "JIT_bugzilla", "JIT_columba", "JIT_jdt", "JIT_mozilla", "JIT_platform", "JIT_postgres", "NASA_cm1",
				"NASA_jm1", "NASA_kc1", "NASA_kc2", "NASA_pc1", "PROMISE_ant-1.5", "PROMISE_ant-1.6", "PROMISE_ant-1.7", "PROMISE_camel-1.2", "PROMISE_camel-1.4", "PROMISE_camel-1.6",
				"PROMISE_ivy-1.4", "PROMISE_ivy-2.0", "PROMISE_jedit-3.2", "PROMISE_jedit-4.0", "PROMISE_jedit-4.1", "PROMISE_log4j-1.0", "PROMISE_log4j-1.1", "PROMISE_lucene-2.0",
				"PROMISE_lucene-2.2", "PROMISE_lucene-2.4", "PROMISE_poi-1.5", "PROMISE_poi-2.5", "PROMISE_poi-3.0", "PROMISE_synapse-1.0", "PROMISE_synapse-1.1", "PROMISE_synapse-1.2",
				"PROMISE_xalan-2.4", "PROMISE_xalan-2.5", "PROMISE_xerces-1.2", "PROMISE_xerces-1.3", "Relink_Apache", "Relink_Safe", "Relink_Zxing" };

		int dataname_col = 0;
		int approachname_col = 3;
		int evaluation_measure = 2;
	
		List<List<String>> allData = readCSV(path + baselinePath + ".csv");

		for (String datasetName : dataset) {
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
//			 ArrayList<Double> b11List = new ArrayList<Double>(); // CFS
			// ArrayList<Double> b12List = new ArrayList<Double>(); // WFS
			ArrayList<Double> b13List = new ArrayList<Double>(); // VC and RR
			for (List<String> newLine : allData) {
				List<String> list = newLine;
				if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("None")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b1List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("Default-PCA")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b2List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("NSVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b3List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("NSVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b4List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("NSVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b5List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("NSVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b6List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("SVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b7List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("SVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b8List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("SVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b9List.add(Double.valueOf(list.get(evaluation_measure)));
				} else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("SVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b10List.add(Double.valueOf(list.get(evaluation_measure)));
				}
				else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("VCRR")) { 
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						b13List.add(Double.valueOf(list.get(evaluation_measure)));
				}
//				else if (list.get(dataname_col).contains(datasetName) && list.get(approachname_col).equals("CFS-BestFirst")) { 
//					if (list.get(evaluation_measure).equals("NaN"))
//						continue;
//					else
//						b11List.add(Double.valueOf(list.get(evaluation_measure)));
//				}

			}
			try {
				System.out.println(datasetName + "size of None list : " + b1List.size());
				saveAverageCSV(baselinePath, datasetName, averageArray(b1List), averageArray(b2List), averageArray(b3List), averageArray(b4List), averageArray(b5List), averageArray(b6List),
						averageArray(b7List), averageArray(b8List), averageArray(b9List), averageArray(b10List), averageArray(b13List));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error writing average to csv");
				System.exit(-1);
			}

		}

	}
	// DT_4_Fmeasure_5_multicollinearity_with_None_thres_10.0_6_average.csv
	public void saveAverageCSV(String baselinePath, String dataset, Double b1Average, Double b2Average, Double b3Average, Double b4Average, Double b5Average, Double b6Average, Double b7Average,
			Double b8Average, Double b9Average, Double b10Average, Double b13Average) throws Exception {
		FileWriter writer = new FileWriter(path + baselinePath + "_6_average.csv", true);
		// Add header for R studio when the first data set
		if (dataset.equals("AEEEM_EQ")) {
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
			// baselineList.add("WFS-BestFirst");
			baselineList.add("VCRR");
//			baselineList.add("CFS-BestFirst");
			CSVUtils.writeLine(writer, baselineList);
		}
		CSVUtils.writeLine(writer, Arrays.asList(dataset, String.valueOf(b1Average), String.valueOf(b2Average), String.valueOf(b3Average), String.valueOf(b4Average), String.valueOf(b5Average),
				String.valueOf(b6Average), String.valueOf(b7Average), String.valueOf(b8Average), String.valueOf(b9Average), String.valueOf(b10Average), String.valueOf(b13Average)));
		writer.flush();
		writer.close();
	}
	
	public void saveRankingCSV(String baselinePath) {
	    //출력 스트림 생성
        BufferedWriter bufWriter = null;
        ArrayList<Double> datasetAverageList = new ArrayList<Double>();
        try{
            bufWriter = Files.newBufferedWriter(Paths.get(path + baselinePath + "_7_ranking.csv"));
            //csv파일 읽기
            List<List<String>> allData = readCSV(path + baselinePath + "_6_average.csv");
            
            for(List<String> newLine : allData){
            		if(newLine.contains("None")) continue; // average 파일에 헤더 추가해서 
                List<String> list = newLine;
                for(int i = 1; i < list.size(); i++) {
                		datasetAverageList.add(Double.valueOf(list.get(i)));
                }
                bufWriter.write(list.get(0));
                bufWriter.write(",");
                for(int rank : AverageArray.ranking(datasetAverageList)){
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
//		ArrayList<Double> b11List = new ArrayList<Double>();
//		ArrayList<Double> b12List = new ArrayList<Double>();
		ArrayList<Double> b13List = new ArrayList<Double>();
        BufferedWriter bufWriter = null;

        try{
            bufWriter = Files.newBufferedWriter(Paths.get(path + baselinePath + "_8_ranking_average.csv"));

            List<List<String>> allData = readCSV(path + baselinePath + "_7_ranking.csv");
            
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
//                		if(i == 11) b11List.add(Double.valueOf(list.get(i)));
//                		if(i == 12) b12List.add(Double.valueOf(list.get(i)));
                		if(i == 11) b13List.add(Double.valueOf(list.get(i)));
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
//	        		if(i == 11) bufWriter.write(String.valueOf(averageArray(b11List)));
//	        		if(i == 12) bufWriter.write(String.valueOf(averageArray(b12List))); 
	        		if(i == 11) bufWriter.write(String.valueOf(averageArray(b13List)));
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
	public void saveProjectAverageCSV(String baselinePath) {
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
//		ArrayList<Double> b12List = new ArrayList<Double>(); // CFS
        BufferedWriter bufWriter = null;

        try{
            bufWriter = Files.newBufferedWriter(Paths.get(path + baselinePath + "_9_project_average.csv"));

            List<List<String>> allData = readCSV(path + baselinePath + "_6_average.csv");
            boolean headerRowFlag = true;
            for(List<String> newLine : allData){
				if(headerRowFlag == true) {
					headerRowFlag = false;
        				continue;
            		}
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
//                		if(i == 12) b12List.add(Double.valueOf(list.get(i)));
                }
            }
            
//            for(int i = 0; i < numberOfApproaches + 1; i++) {
//            		if(i == 0) {
//            			bufWriter.write(",");
//            		}
//            		else {
//            			bufWriter.write("baseline" + i);
//                		bufWriter.write(",");
//            		}
//            }
//            bufWriter.newLine();
            
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
//	        		if(i == 12) bufWriter.write(String.valueOf(averageArray(b12List)));
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


	public double averageArray(ArrayList<Double> list) {
		int NaNCnt = 0;
		if (list.size() == 0) {
			System.out.println("Can not average list because list size is 0, check your list!");
			// System.exit(-1);
		}
		double sum = 0;
		double average = 0;
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isNaN()) {
				sum += list.get(i);
			} else {
				NaNCnt++;
			}
		}
		average = sum / (list.size() - NaNCnt);
		return average;
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


	
	// savedDataHavingMulticollinearity(inputPath, "10.0", "None")
	public ArrayList<String> savedDataHavingMulticollinearity(String inputPath, String thres, String targetApproachName) {
		int idx = 0;

		if (thres.equals("10.0"))
			idx = 20;
		else if (thres.equals("5.0"))
			idx = 21;
		else if (thres.equals("4.0"))
			idx = 22;
		else if (thres.equals("2.5"))
			idx = 23;
		else {
			System.out.println("Wrong threshold of VIF");
			System.exit(-1);
		}

		ArrayList<String> listOfMulticollinearityData = new ArrayList<>();
		List<List<String>> allData = CSVUtils.readCSV(inputPath);
		for (List<String> newLine : allData) {
			List<String> list = newLine;
			if (list.get(approachname_col).equals(targetApproachName) && list.get(idx).equals(thres)) {
				listOfMulticollinearityData.add(list.get(dataname_col));
			}
		}
		Double numberOfMulticollinearity = Double.valueOf(listOfMulticollinearityData.size());
		System.out.println("Input path: " + inputPath + " / Thres: " + thres + " / Approach name: " + targetApproachName + " / Number: " + listOfMulticollinearityData.size() + " / Ratio: "
				+ numberOfMulticollinearity / 4500.0);
		return listOfMulticollinearityData;
	}

	public void savedPerformanceOfApproaches(String inputPath, String outputPath, ArrayList<String> listOfMulticollinearityData, String thresholdVIF, int positionMeasurementColumn)
			throws IOException {
		int evaluation_measure = positionMeasurementColumn;

		if (positionMeasurementColumn == 4)
			measurementName = "_1_AUC";
		else if (positionMeasurementColumn == 1)
			measurementName = "_2_Precision";
		else if (positionMeasurementColumn == 2)
			measurementName = "_3_Recall";
		else if (positionMeasurementColumn == 3)
			measurementName = "_4_Fmeasure";
		else if (positionMeasurementColumn == 5)
			measurementName = "_5_MCC";

		List<List<String>> allData = CSVUtils.readCSV(inputPath);
		FileWriter writer = new FileWriter(outputPath + measurementName + "_5_multicollinearity_with_None_thres_" + thresholdVIF + ".csv", true);

		for (String AbsoluteSourcePath : listOfMulticollinearityData) {
			// extract detail information
			File file = new File(AbsoluteSourcePath); // absolute path
			String multicollinearity_sourcepath = file.getName(); // only file name
			multicollinearity_sourcepath = multicollinearity_sourcepath.replace(".arff", "");
			String[] multicollinearity_sourcepath_array;
			multicollinearity_sourcepath_array = multicollinearity_sourcepath.split("_");
			String project = multicollinearity_sourcepath_array[0];
			String dataset = multicollinearity_sourcepath_array[1];
			String iter = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 2];
			String fold = multicollinearity_sourcepath_array[multicollinearity_sourcepath_array.length - 1];
			// System.out.println("project: " + project + " / dataset: " + dataset + " /
			// iter: " + iter + " / fold: " + fold);
			// search the dataset in the inputPath
			for (List<String> newLine : allData) {
				List<String> list = newLine;
				if (list.get(dataname_col).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("None")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "None"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_PCA_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("Default-PCA")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "Default-PCA"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "NSVIF10"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "NSVIF5"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "NSVIF4"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_NONSTEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("NSVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "NSVIF2.5"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_10_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF10")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "SVIF10"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "SVIF5"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_4_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF4")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "SVIF4"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_VIF_STEPWISE_2_5_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("SVIF2.5")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "SVIF2.5"));
				} else if (list.get(dataname_col).contains(project + "_" + dataset + "_" + iter + "_" + fold + ".arff") && list.get(approachname_col).equals("VCRR")) {
					if (list.get(evaluation_measure).equals("NaN"))
						continue;
					else
						CSVUtils.writeLine(writer, Arrays.asList(multicollinearity_sourcepath, thresholdVIF, String.valueOf(Double.valueOf(list.get(evaluation_measure))), "VCRR"));
				}
			}

		}
		writer.flush();
		writer.close();
		System.out.println("Finish saving the performance of approaches!");
	}

}
