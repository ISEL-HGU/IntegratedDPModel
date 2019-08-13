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
import java.util.ArrayList;
import java.util.HashMap;

public class NemenyiCD {
	static String rootPath = "/Users/eunjiwon/Desktop/nemenyi/";
	static String[] filenameArray = {
			"RF.csv",
	};
	
	public static void main(String[] args) throws IOException {
		
		for(String filename : filenameArray) {
			ArrayList<Integer> connectedLineList[] = new ArrayList[13];
			for(int i = 0; i < 13; i++) {
				connectedLineList[i] = new ArrayList<Integer>();
			}
			List<List<String>> allData = readText(rootPath + filename);
			for(int i = 0; i < allData.size(); i++) {
				if(i == 0) continue;
				List<String> elements = allData.get(i);
				for(int j = 0; j < elements.size(); j++) {
					if(j == 0) continue;
					String element = elements.get(j);
					if (!element.equals("-")) {
						double temp = Double.parseDouble(element); 
						if(temp > 0.05) {
//							System.out.println(temp);
							connectedLineList[i+1].add(j);
							connectedLineList[j].add(i+1);
						}
					}
				}
			}
			// true argument means appending file
			FileWriter fw = new FileWriter(rootPath + "approaches_nemenyi_results.txt", true);
			BufferedWriter bufWriter = new BufferedWriter(fw);
			bufWriter.write(filename);
			bufWriter.newLine();
			for (int i = 1; i < 13; i++) {
				System.out.println(i + " -> " + connectedLineList[i]);
				bufWriter.write(i + " -> " + connectedLineList[i]);
				bufWriter.newLine();
			}
			bufWriter.close();
			fw.close();
		}
		
	
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
