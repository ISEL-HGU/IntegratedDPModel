#!/bin/sh
for string in "NASA_cm1" "NASA_jm1" "NASA_kc1" "NASA_kc2" "NASA_pc1" "PROMISE_ant-1.5" "PROMISE_ant-1.6" "PROMISE_ant-1.7" "PROMISE_camel-1.2" "PROMISE_camel-1.4" "PROMISE_camel-1.6" "PROMISE_ivy-1.4" "PROMISE_ivy-2.0" "PROMISE_jedit-3.2" "PROMISE_jedit-4.0" "PROMISE_jedit-4.1" "PROMISE_log4j-1.0" "PROMISE_log4j-1.1" "PROMISE_lucene-2.0" "PROMISE_lucene-2.2" "PROMISE_lucene-2.4" "PROMISE_poi-1.5" "PROMISE_poi-2.5" "PROMISE_poi-3.0" "PROMISE_synapse-1.0" "PROMISE_synapse-1.1" "PROMISE_synapse-1.2" "PROMISE_xalan-2.4" "PROMISE_xalan-2.5" "PROMISE_xerces-1.2" "PROMISE_xerces-1.3";
    do
<<<<<<< HEAD
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_data/${string}.arff -d /home/eunjiwon/Git/EJTool/fs_cross_validation_data -a ${string}.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_PCA.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_PCA.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_2_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_2_5.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_4.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_4.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_5.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_10.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_10.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_2_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_2_5.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_4.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_4.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_5.arff -i 10 -f 10 
    #./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_10.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_10.arff -i 10 -f 10 
=======
    ./EJTool -s /home/eunjiwon/Git/EJTool/VC_RR_data/${string}.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_PCA.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_PCA.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_2_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_2_5.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_4.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_4.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_5.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_NONSTEPWISE_10.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_NONSTEPWISE_10.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_2_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_2_5.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_4.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_4.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_5.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_5.arff -i 10 -f 10 
    ./EJTool -s /home/eunjiwon/Git/EJTool/origin_pca_vif_data/${string}_VIF_STEPWISE_10.arff -d /home/eunjiwon/Git/EJTool/cross_validation_data -a ${string}_VIF_STEPWISE_10.arff -i 10 -f 10 
>>>>>>> 7d3f8f1f525bff440b7a55908825ddb8bf0eaa87
done

