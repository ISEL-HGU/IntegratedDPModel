#!/bin/sh
# type3
#./EJTool -s /home/eunjiwon/Git/EJTool/fs_cross_validation_data/ -t 3 -c /home/eunjiwon/Git/EJTool/multi_results/t3_LR_result.csv -i 10 -f 10 -m weka.classifiers.functions.Logistic -d 1 -p 20
./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/LSTM_LR_result.csv -i 100 -f 10 -m weka.classifiers.functions.Logistic -d 1 -p 25
#./EJTool -s /home/eunjiwon/Git/EJTool/fs_cross_validation_data/ -t 3 -c /home/eunjiwon/Git/EJTool/multi_results/t3_DT_result.csv -i 10 -f 10 -m weka.classifiers.trees.J48 -d 1 -p 25
