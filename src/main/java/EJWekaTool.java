import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;

import weka.core.Instances;

import java.util.Random;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EJWekaTool {

	public static void main(String[] args) {
		String[] targetFilelist = { "Relink/Apache.arff", "Relink/Safe.arff", "Relink/Zxing.arff", "AEEEM/EQ.arff", "AEEEM/JDT.arff", "AEEEM/LC.arff", "AEEEM/ML.arff", "AEEEM/PDE.arff" };
		for(int i = 0; i < targetFilelist.length; i++) {
			run(targetFilelist[i]);
		}
		
	}

	public static void run(String targetFile) {
		
		String trainingArffPath = "/Users/eunjiwon/Desktop/Multucollinearity/TransferDefectLearningDataset/Original/" + targetFile;
		try {
			// (1) read file
			BufferedReader reader = new BufferedReader(new FileReader(trainingArffPath));
			Instances trainingData = new Instances(reader);
			// set label index to last index
			trainingData.setClassIndex(trainingData.numAttributes()-1);
			reader.close();
			// (2) no pre-processing(ready for input data to model)
			// (3) make model
			Classifier myModel = new Logistic(); 
			myModel.buildClassifier(trainingData);
			// (4) test set and apply and prediction
			Evaluation eval = new Evaluation(trainingData);
			eval.crossValidateModel(myModel, trainingData, 10, new Random(1));
			// (5) show result
			System.out.println("--------------------------");
			System.out.println(targetFile);
			showSummary(eval,trainingData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showSummary(Evaluation eval,Instances instances) {
		for(int i=0; i<instances.classAttribute().numValues();i++) {
			System.out.println("\n*** Summary of Class " + instances.classAttribute().value(i));
//			System.out.println("Precision " + eval.precision(i));
//			System.out.println("Recall " + eval.recall(i));
			System.out.println("F-Measure " + eval.fMeasure(i));
		}
	}
}
