#!/bin/sh
for projectname in "ace" "ant-ivy" "bigtop" "bval" "camel" "cayenne" "cordova-android" "creadur-rat" "crunch" "deltaspike" "gora" "groovy" "guacamole-client" "incubator-hivemall";
do
./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_developer.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_developer.arff

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_Concat_Single_LSTM_Metric.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_Concat_Single_LSTM_Metric.arff

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_Line_Single_LSTM_Metric.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_Line_Single_LSTM_Metric.arff

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_Master_NGLP_Metric.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_Master_NGLP_Metric.arff

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_Single_NGLP_Metric.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_Single_NGLP_Metric.arff

done




