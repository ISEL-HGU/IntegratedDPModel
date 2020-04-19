#!/usr/bin/env python3
import math
from util import tokenize_data
from ngram import Ngram
import csv
import os.path   

if __name__ == '__main__':
    train_filename = '/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/bval_AllCommitsAddedLines.txt'
    train_data = tokenize_data(train_filename)
    print(train_data)
    ngram = Ngram(3)
    print("TRAINING STARTED...")
    list_of_bigrams, unigram_counts, bigram_counts, list_of_trigrams, trigram_counts = ngram.train(train_data) 
    
    one_gram_prob = ngram.calculate_onegram_prob(unigram_counts) 
    bigram_prob = ngram.calculate_bigram_prob(list_of_bigrams, unigram_counts, bigram_counts)
    trigram_prob = ngram.calculate_trigram_prob(list_of_trigrams, bigram_counts, trigram_counts)
    
    with open("/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP/bval-reference/bval_developer.csv",'r') as csvinput:
        with open("/home/eunjiwon/Git/EJTool/Ngram/nglp_csv/bval_Add_SNGLP_metric.csv", 'w') as csvoutput:
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
                testcommit_name = "/home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/bval/" + commit_hash_key + ".txt"
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

   
