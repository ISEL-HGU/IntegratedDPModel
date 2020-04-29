#!/bin/sh
# Single LSTM
for projectname in "ace" "ant-ivy" "bigtop" "bval" "camel" "cayenne" "cordova-android" "creadur-rat" "crunch" "deltaspike" "gora" "groovy" "guacamole-client" "incubator-dolphinscheduler" "incubator-hivemall";
do
./EJTool -i /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP/label_DP/${projectname}_developer.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Concat_Single_LSTM_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Line_Single_LSTM_Metric.csv
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Master_NGLP_Metric.csv  
./EJTool -i /home/eunjiwon/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Single_NGLP_Metric.csv 
done




