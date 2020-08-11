#!/bin/sh
# type4
./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/LSTM_LR_result.csv -i 100 -f 10 -m weka.classifiers.functions.Logistic -d 1 -p 23
#./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/VC_RR_RF_result.csv -i 10 -f 10 -m weka.classifiers.trees.RandomForest -d 1 -p 23
#./EJTool -s /home/eunjiwon/Git/EJTool/vc_rr_cross_validation_data/ -t 4 -c /home/eunjiwon/Git/EJTool/multi_results/VC_RR_DT_result.csv -i 10 -f 10 -m weka.classifiers.trees.J48 -d 1 -p 23


./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data/ -t 5 -c /home/eunjiwon/Git/EJTool/multi_results/7_baselines_LR_result.csv -i 100 -f 10 -m weka.classifiers.functions.Logistic -d 3 -p 27 -l isBuggy -b buggy

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data/ -t 5 -c /home/eunjiwon/Git/EJTool/multi_results/7_baselines_RF_result.csv -i 100 -f 10 -m weka.classifiers.trees.RandomForest -d 3 -p 27 -l isBuggy -b buggy

./EJTool -s /home/eunjiwon/Git/EJTool/LSTM_cross_validation_data/ -t 5 -c /home/eunjiwon/Git/EJTool/multi_results/7_baselines_DT_result.csv -i 100 -f 10 -m weka.classifiers.trees.J48 -d 3 -p 27 -l isBuggy -b buggy
