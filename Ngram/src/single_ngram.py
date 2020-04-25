#!/usr/bin/env python3
import math
from util import tokenize_data
from ngram import Ngram
import csv
import os.path   

if __name__ == '__main__':
    project_list = ["ace", "ant-ivy", "apex-core", "bigtop", "bval", "camel", "cayenne", "cordova-android", "creadur-rat", "crunch", "deltaspike", "gora", "groovy", "guacamole-client", "incubator-dolphinscheduler", "incubator-hivemall"]
    for project_name in project_list:

        train_filename = '/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/' + project_name+ '_AllCommitsAddedLines.txt'
        train_data = tokenize_data(train_filename)
        print(train_data)
        ngram = Ngram(3)
        print("TRAINING STARTED...")
        list_of_bigrams, unigram_counts, bigram_counts, list_of_trigrams, trigram_counts = ngram.train(train_data) 
        
        one_gram_prob = ngram.calculate_onegram_prob(unigram_counts) 
        bigram_prob = ngram.calculate_bigram_prob(list_of_bigrams, unigram_counts, bigram_counts)
        trigram_prob = ngram.calculate_trigram_prob(list_of_trigrams, bigram_counts, trigram_counts)
        #input_csv_metric_file /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP/label_DP/${projectname}_developer.csv
        #output_csv_metric_file ./data/test/${projectname}_Line_Single_LSTM_Metric.csv
        # /Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/${projectname}_Single_NGLP_Metric.csv
        #test_file /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/${projectname}/
        with open("/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP/label_DP/" + project_name + "_developer.csv",'r') as csvinput:
            with open("/Git/SoftwareDefectPredictionMetricUsingDeepLearning/data/test/" + project_name + "_Single_NGLP_Metric.csv", 'w') as csvoutput:
                writer = csv.writer(csvoutput, lineterminator='\n')
                reader = csv.reader(csvinput)
                all = []
                row = next(reader)
                # print(row[0])
                row.append('NGLP')
                all.append(row)
                header_flag = 0
                for row in reader:
                    if header_flag == 0: 
                        header_flag = 1
                        continue # skip header row
                    commit_hash_key = row[20].split('-')[0]
                    testcommit_name = "/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/" + project_name + "/" + commit_hash_key + ".txt"
                    if os.path.exists(testcommit_name):
                        # print(testcommit_name)
                        test_data = tokenize_data(testcommit_name) 
                        # print(test_data)
                        NGLP = ngram.trigram_NGLP(test_data)
                        print(testcommit_name, NGLP)
                        row.append(NGLP)
                        all.append(row)
                    else:
                        print("Error! ", testcommit_name, " does not exist!!!!! ")

                writer.writerows(all)

        print("Finish!")

   
