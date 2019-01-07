package edu.handong.csee.isel.weka;

import weka.classifiers.evaluation.RegressionAnalysis;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VIF {
   private Instances instances = null;
   private int attrIndex = -1; // 이게 무슨 의미이지? 
   private boolean verbose = false;
   
   public static void main(String[] args) {
      String fileName = null;
      int attrIndex = -1;
      VIF vif = null;
      boolean verbose = false;
      fileName = "/Users/eunjiwon/Desktop/Multucollinearity/TransferDefectLearningDataset/Original/AEEEM/EQ.arff";
      verbose = true; 
      
//      for (int i = 0; i < args.length; i++) {
//         if (args[i].equals("-f")) {
//            fileName = args[++i];
//            i++;
//         }
//         else if (args[i].equals("-v")) {
//      		verbose = true;   
//         }
//         else {
//         	attrIndex = new Integer(args[i]).intValue();
//         }
//      }
      try {
		  if (attrIndex != -1) {
			 vif = new VIF(fileName,attrIndex);
		  }
		  else {
			vif = new VIF(fileName);
		  }
      }
      catch (IOException ioex) { ioex.printStackTrace(); }
      if (verbose) vif.setVerbose(verbose);
      try {
      	double[] result = vif.getVIFs();
      }
      catch (Exception ex) { ex.printStackTrace(); }
   }
	
	public VIF(String fileName) throws IOException {
		BufferedReader rdr = new BufferedReader(new FileReader(fileName));
		instances = new Instances(rdr);
	}
	
	public VIF(Instances instances) {
	    this.instances = instances;
	}
	
	public VIF(String fileName, int attrIndex) throws IOException {
	   this(fileName);
	   this.attrIndex = attrIndex;
	}
	
	public VIF(Instances instances, int attrIndex) {
	   this.instances = instances;
	   this.attrIndex = attrIndex;
	}
	
	public double[] getVIFs() throws Exception {
	   double[] result = calculateVIF(instances,attrIndex);
	   return result;
	}
	
	public double[] calculateVIF(Instances instances,int attrIndex) throws Exception {

	   Remove rm = new Remove();
	   rm.setAttributeIndices("last"); // 와우 이게 지금 마지막 클래스 지우는거네... 
	   rm.setInputFormat(instances);
	   instances = Filter.useFilter(instances, rm);

	   int n = instances.numAttributes();
	   System.out.println("n = " + n);
	   double[] vifs = new double[n];
	   if (verbose)
	      System.out.println("Relation: " + instances.relationName());
	   if (attrIndex == -1) {
	      for (int i = 0; i < vifs.length; i++) {        
	         instances.setClassIndex(i);
	         AccessibleLinearRegression regressor = new AccessibleLinearRegression();
	         regressor.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
	         regressor.setEliminateColinearAttributes(false);
	         regressor.buildClassifier(instances);
	         double r2 = regressor.getRSquared(instances);
	         vifs[i] = 1d / (1d - r2);
	         if (verbose)
	         	System.out.println(i + "\t" + instances.attribute(i).name() + "\t" + vifs[i]);
	      }
	   }
	   else {
	   }
	   return vifs;
	}
	
	public void setVerbose(boolean verbose) {
	   this.verbose = verbose;
	}
}

class AccessibleLinearRegression extends LinearRegression {
	
	public double getRSquared(Instances data) throws Exception {
	  // calculate R^2 
      double se = calculateSE(data, m_SelectedAttributes, m_Coefficients);
      return RegressionAnalysis.calculateRSquared(data, se);
    }
    
    public double calculateSE(Instances data, boolean[] selectedAttributes,
      double[] coefficients) throws Exception {
      double mse = 0;
      for (int i = 0; i < data.numInstances(); i++) {
        double prediction =
        regressionPrediction(data.instance(i), selectedAttributes,
          coefficients);
        double error = prediction -data.instance(i).classValue();
        mse += error * error;
      }
      return mse;
    }
}