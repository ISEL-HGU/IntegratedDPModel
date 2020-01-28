package edu.handong.csee.isel.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareCommitHash {
	static String root_path = "/Users/eunjiwon/Desktop/NGLP";
	static String dataset_name = "camel";
	
//	camel 429
//	eagle 5 
//	flink 902
//	groovy 173
//	jena 100 
//	juddi 2
//	metamodel 2
//	nutch 41
	
	static String path1 = root_path +  "/BIC/BIC_" + dataset_name + ".csv"; // 버그레이블 정보 있는 파일 
	static String path2 = root_path + "/DP" + dataset_name + "/" + dataset_name + ".csv";  // middle and end point

//	static String path2 = root_path + "/" + dataset_name + "/" + dataset_name + ".csv"; // start and middle point
//	static String path3 = root_path + "list.txt";
//	static String path4 = root_path + "labeled_httpcomponents-client.csv";
	
	public static void main(String[] args) throws IOException {
		CompareCommitHash myCompareCommitHash = new CompareCommitHash();
		// run method
		myCompareCommitHash.run(path1, path2);
	}
	
	public void run(String path1, String path2) throws IOException {
		// Read two files which are compared with each other
		List<List<String>> target_data1 = readCSV(path1);
		List<List<String>> target_data2 = readCSV(path2);
		
//		List<List<String>> target_data3 = readCSV(path3); // 290 commit
//		List<List<String>> target_data4 = readCSV(path4); // 291 commit
		
//		 Count matching commit hash value
//		countMatchCommitHash(target_data1, target_data2);
		
//		 Labelling class label using mapping file
		labelling(target_data1, target_data2);
		
		// Find difference commit between list.txt and labeled_httpcomponents-client.csv
//		FindDifferecnceCommit(target_data3, target_data4);
		
	}
	
	public static void FindDifferecnceCommit(List<List<String>> target3, List<List<String>> target4) {
		ArrayList<String> allCommitList = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
//		System.out.println(target3.size());
//		System.out.println(target4.size());
		for (List<String> ele: target4) {
			allCommitList.add(ele.get(0)); // 291
			temp.add(ele.get(0)); // 291
		}
//		System.out.println(allCommitList);

		for (List<String> ele: target3) {
			String removed_extension = ele.get(0).replace(".txt", "");
			if(allCommitList.contains(removed_extension))
				temp.remove(removed_extension);
		}
//		System.out.println("-------------");
//		System.out.println(temp.size());
	}
	
	
	// Labelling class label using mapping file
	public static void labelling(List<List<String>> target1, List<List<String>> target2) throws IOException {
		BufferedWriter bufWriter = null;
		int match_count = 0;
		bufWriter = Files.newBufferedWriter(Paths.get(root_path + "/DP" + dataset_name + "/labeled_" + dataset_name + ".csv"));
		for (int i = 0; i < target2.size(); i++) { // 362
			boolean isMatch = false;
			if(i == 0) continue;
			List<String> list1 = target2.get(i);
			String total_commit_hash = list1.get(0);
		    for(String data : list1){
                bufWriter.write(data);
                bufWriter.write(",");
            }
		    for (int j = 0; j < target1.size(); j++) { // 86
				if(j == 0) continue;
				List<String> list2 = target1.get(j);
				String bic_commit_hash = list2.get(0);
				// Compare two commit hash values
				if (total_commit_hash.equals(bic_commit_hash)) {
					isMatch = true;
					break;
				}
			}
		    if(isMatch == true) {
		    		bufWriter.write("1");
				bufWriter.newLine();
				match_count += 1;
				System.out.println("Match! The commit hash is : " + total_commit_hash);
		    }
		    else {
		    		bufWriter.write("0");
				bufWriter.newLine();
		    }
		    
		}
		bufWriter.close();
		System.out.println("# of match is : " + match_count);
	}
	
	
	// Count matching commit hash value
	public static void countMatchCommitHash(List<List<String>> target1, List<List<String>> target2) {
		int count = 0;
		for (int i = 0; i < target1.size(); i++) {
			if(i == 0) continue;
			List<String> list1 = target1.get(i);
			String total_commit_hash = list1.get(0);
//			System.out.println(total_commit_hash);
			for (int j = 0; j < target2.size(); j++) {
				if(j == 0) continue;
				List<String> list2 = target2.get(j);
				String bic_commit_hash = list2.get(0);
//				System.out.println(bic_commit_hash);
				
				// Compare two commit hash values
				if (total_commit_hash.equals(bic_commit_hash)) {
					count = count + 1;
					System.out.println("Match! The commit hash is : " + total_commit_hash);
				}
				
			}
		}
		System.out.println("The target1 dataset's name : " + path1);
		System.out.println("The target1 dataset's size : " + target1.size());
		System.out.println("The target2 dataset's name : " + path2);
		System.out.println("The target2 dataset's size : " + target2.size());
		System.out.println("Matching count is : " + count);
	}
	
	public static void printData(List<List<String>> allData) {
		for(List<String> newLine : allData) {
            List<String> list = newLine;
              for(String element : list) {
            	      System.out.print(element);
              }
        	}
	}
	
    public static List<List<String>> readCSV(String file_path){
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        try{
            br = Files.newBufferedReader(Paths.get(file_path));
            String line = "";
            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();  
                String array[] = line.split(",");
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
