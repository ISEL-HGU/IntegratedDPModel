#!/bin/sh
for string in "NASA_cm1" "NASA_jm1.arff" "NASA_kc1.arff" "NASA_kc2.arff" "NASA_pc1.arff" "PROMISE_ant-1.5.arff" "PROMISE_ant-1.6.arff" "PROMISE_ant-1.7.arff" "PROMISE_camel-1.2.arff" "PROMISE_camel-1.4.arff" "PROMISE_camel-1.6.arff" "PROMISE_ivy-1.4.arff" "PROMISE_ivy-2.0.arff" "PROMISE_jedit-3.2.arff" "PROMISE_jedit-4.0.arff" "PROMISE_jedit-4.1.arff" "PROMISE_log4j-1.0.arff" "PROMISE_log4j-1.1.arff" "PROMISE_lucene-2.0.arff" "PROMISE_lucene-2.2.arff" "PROMISE_lucene-2.4.arff" "PROMISE_poi-1.5.arff" "PROMISE_poi-2.5.arff" "PROMISE_poi-3.0.arff" "PROMISE_synapse-1.0.arff" "PROMISE_synapse-1.1.arff" "PROMISE_synapse-1.2.arff" "PROMISE_xalan-2.4.arff" "PROMISE_xalan-2.5.arff" "PROMISE_xerces-1.2.arff" "PROMISE_xerces-1.3.arff";
    do
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 1 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_PCA.arff -th 0
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 2 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_2_5.arff -th 2.5
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 2 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_4.arff -th 4
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 2 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_5.arff -th 5
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 2 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_10.arff -th 10
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 3 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_2_5.arff -th 2.5
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 3 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_4.arff -th 4
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 3 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_5.arff -th 5
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -t 3 -d /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_10.arff -th 10 
done


