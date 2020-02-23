package edu.handong.csee.isel.weka;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import weka.core.Instances;

public class SaveCrossValidation {
	String source;
	String dest;
	String iter;
	String fold;
	String arffFileName;
	boolean help = false;
	
	public static void main(String[] args) {
		SaveCrossValidation myRunner = new SaveCrossValidation();
		myRunner.run(args);
	}
	
	private void run(String[] args) {
		Options options = createOptions();

		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			Instances instances = loadArff(source);

			File createdDir = new File(dest);
			if(!createdDir.exists()){
				if(!(new File(dest).mkdirs())){
					System.err.println(dest +" is not created");
					System.exit(0);
				}
			}

			for(int i=0;i<Integer.parseInt(iter);i++){

				instances.randomize(new Random(i)); 
				instances.stratify(Integer.parseInt(fold));

				for(int n=0;n<Integer.parseInt(fold);n++){
					Instances testInstances = instances.testCV(Integer.parseInt(fold), n);
					writeAFile(testInstances + "", dest + File.separator + arffFileName.replace(".arff", "") + "_" + i + "_" + n + ".arff");
				}
			}
			

		}

		
	}
	
	/**
	 * Load Instances from arff file. Last attribute will be set as class attribute
	 * @param path arff file path
	 * @return Instances
	 */
	public static Instances loadArff(String path){
		Instances instances=null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			instances = new Instances(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		instances.setClassIndex(instances.numAttributes()-1);

		return instances;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName){
		try {
			File file= new File(targetFileName);
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos=new DataOutputStream(fos);
			
			for(String line:lines){
				dos.write((line+"\n").getBytes());
			}
			//dos.writeBytes();
			dos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeAFile(String lines, String targetFileName){
		try {
			File file= new File(targetFileName);
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos=new DataOutputStream(fos);
			
			dos.writeBytes(lines);
				
			dos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	Options createOptions(){

		// create Options object
		Options options = new Options();

		// add options
		options.addOption(Option.builder("s").longOpt("source")
				.desc("source arff file name")
				.hasArg()
				.argName("file")
				.required()
				.build());

		options.addOption(Option.builder("i").longOpt("iter")
				.desc("number of iteration")
				.hasArg()
				.required()
				.argName("number of cross-validation iterations")
				.build());
		
		options.addOption(Option.builder("f").longOpt("fold")
				.desc("the number of fold")
				.hasArg()
				.required()
				.argName("the number of cross-validation folds")
				.build());
		
		options.addOption(Option.builder("d").longOpt("destination")
				.desc("destination directory")
				.hasArg()
				.required()
				.argName("the number of cross-validation folds")
				.build());
		
		options.addOption(Option.builder("a").longOpt("arff")
				.desc("arff file name")
				.hasArg()
				.required()
				.argName("arff file name")
				.build());

		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());


		return options;
	}

	boolean parseOptions(Options options,String[] args){

		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			source = cmd.getOptionValue("s");
			help = cmd.hasOption("h");
			dest = cmd.getOptionValue("d"); 
			iter = cmd.getOptionValue("i");
			fold = cmd.getOptionValue("f");
			arffFileName = cmd.getOptionValue("a");


		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		return true;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Multicollineaity paper experiment tool";
		String footer ="\nPlease report issues at https://github.com/HGUISEL/EJTool/issues";
		formatter.printHelp("CLIExample", header, options, footer, true);
	}

}
