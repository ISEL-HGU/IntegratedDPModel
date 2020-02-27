#!/bin/sh
for string in "AEEEM_LC"; 
    do
    ./EJTool -s /home/eunjiwon/Git/EJTool/VC_RR_data/${string}.arff -d /home/eunjiwon/Git/EJTool/AEEEM_LC_vc_rr_cross_validation_data -a ${string}.arff -i 10 -f 10 
done
