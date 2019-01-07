package edu.handong.csee.isel.weka;

import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.evaluation.RegressionAnalysis;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;

import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class VIF {
   private Instances instances = null;
   private int attrIndex = -1; // 이게 무슨 의미이지? 
   private boolean verbose = false;
   
   public static void main(String[] args) {
      String fileName = null;
      int attrIndex = -1;
      VIF vif = null;
      boolean verbose = false;
      fileName = "/Users/eunjiwon/Desktop/Multucollinearity/Data/EQ.arff";
      verbose = true; 
      
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
	   ArrayList<Integer> indices = new ArrayList<>();
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
	         if (vifs[i] >= 10) { 
	        	 	// 그 i번째의 속성 값을 삭제 해야 한다.  
	        	 	indices.add(i);
//	        	 	System.out.println("\t" + vifs[i]);
	         }
//	         if (verbose)
//	         	System.out.println(i + "\t" + instances.attribute(i).name() + "\t" + vifs[i]);
	        
	      }

		  int[] array = new int[indices.size()];
		  int size=0;
		  for(int temp : indices){
		    array[size++] = temp;
		  }
		  
	      Remove removeFilter = new Remove();
	      removeFilter.setAttributeIndicesArray(array);
	      //removeFilter.setInvertSelection(true); array에 있는 인덱스를 뺀 거다.
	      removeFilter.setInputFormat(instances);
	      Instances newData = Filter.useFilter(instances, removeFilter);
	      
	      for (int j = 0; j < newData.numAttributes(); j++) {
      	 	System.out.println(j + "\t" + newData.attribute(j).name() + "\t");
	      }  
	      
	   }
	   else {
	   }
	   return vifs;
	}
	public Instances ApplyVIF(Instances instances,int attrIndex) throws Exception {
		   Instances newData = null;
		   ArrayList<Integer> indices = new ArrayList<>();
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
		         if (vifs[i] >= 10) { 
		        	 	// 그 i번째의 속성 값을 삭제 해야 한다.  
		        	 	indices.add(i);
		        	 	System.out.println("\t" + vifs[i]);
		         }
//		         if (verbose)
//		         	System.out.println(i + "\t" + instances.attribute(i).name() + "\t" + vifs[i]);
		        
		      }

			  int[] array = new int[indices.size()];
			  int size=0;
			  for(int temp : indices){
			    array[size++] = temp;
			  }
			  
		      Remove removeFilter = new Remove();
		      removeFilter.setAttributeIndicesArray(array);
		      //removeFilter.setInvertSelection(true); array에 있는 인덱스를 뺀 거다.
		      removeFilter.setInputFormat(instances);
		      newData = Filter.useFilter(instances, removeFilter);
		      
		      for (int j = 0; j < newData.numAttributes(); j++) {
	      	 	System.out.println(j + "\t" + newData.attribute(j).name() + "\t");
		      }  
		      
		   }
		   else {
		   }
		   return newData;
		}
	
	public void setVerbose(boolean verbose) {
	   this.verbose = verbose;
	}

}

