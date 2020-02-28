#!/bin/sh
# type1
./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/t1_DT_result.csv -i 10 -f 10 -m weka.classifiers.trees.J48 -d 1 -p 25
./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/t1_LR_result.csv -i 10 -f 10 -m weka.classifiers.functions.Logistic -d 1 -p 25
#./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/multi_results/t1_RF_result.csv -i 10 -f 10 -m weka.classifiers.trees.RandomForest -d 1 -p 25
