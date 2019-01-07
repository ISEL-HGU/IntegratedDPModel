package edu.handong.csee.isel.weka;

import weka.classifiers.evaluation.RegressionAnalysis;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

class AccessibleLinearRegression extends LinearRegression {
	
	public double getRSquared(Instances data) throws Exception {
	  // calculate R^2 
	  // se 가 RSS 인듯 
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