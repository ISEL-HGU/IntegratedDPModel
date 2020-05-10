#!/bin/sh/
for projectname in "ant-ivy" "bigtop" "bval" "camel" "cayenne" "creadur-rat" "deltaspike" "gora" "guacamole-client" "incubator-hivemall"; 
do
./EJTool -i /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP/label_DP/${projectname}_developer.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Concat_Single_LSTM_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Line_Single_LSTM_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Master_NGLP_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Single_NGLP_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Concat_Master_LSTM_Metric.csv
done