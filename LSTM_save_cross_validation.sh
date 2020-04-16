#!/bin/sh
for projectname in "camel" "eagle" "groovy" "jena" "juddi" "metamodel" "nutch";
do
./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_origin_data_epoch_20_batchsize_32/${projectname}_Add_LSTM_metric.arff -d /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data -i 100 -f 10 -a ${projectname}_Add_LSTM_metric.arff
done
