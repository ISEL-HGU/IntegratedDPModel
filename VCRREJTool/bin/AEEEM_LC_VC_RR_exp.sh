#!/bin/sh
# type4
./EJTool -s /home/eunjiwon/Git/EJTool/AEEEM_LC_vc_rr_cross_validation_data/ -t 4 -c /home/eunjiwon/Git/EJTool/multi_results/AEEEM_LC_VC_RR_LR_result.csv -i 10 -f 10 -m weka.classifiers.functions.Logistic -d 1 -p 23
./EJTool -s /home/eunjiwon/Git/EJTool/AEEEM_LC_vc_rr_cross_validation_data/ -t 4 -c /home/eunjiwon/Git/EJTool/multi_results/AEEEM_LC_VC_RR_RF_result.csv -i 10 -f 10 -m weka.classifiers.trees.RandomForest -d 1 -p 23
./EJTool -s /home/eunjiwon/Git/EJTool/AEEEM_LC_vc_rr_cross_validation_data/ -t 4 -c /home/eunjiwon/Git/EJTool/multi_results/AEEEM_LC_VC_RR_DT_result.csv -i 10 -f 10 -m weka.classifiers.trees.J48 -d 1 -p 23
