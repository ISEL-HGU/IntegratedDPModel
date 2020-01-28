#!/usr/bin/env python3
import math
from util import tokenize_data
from ngram import Ngram
import csv
import os.path   

if __name__ == '__main__':
    train_filename = '../data/AllCommitAddLines.txt'
    train_data = tokenize_data(train_filename)
    ngram = Ngram(3)
    print("TRAINING STARTED...")
    list_of_bigrams, unigram_counts, bigram_counts, list_of_trigrams, trigram_counts = ngram.train(train_data) 
    
    one_gram_prob = ngram.calculate_onegram_prob(unigram_counts) 
    bigram_prob = ngram.calculate_bigram_prob(list_of_bigrams, unigram_counts, bigram_counts)
    trigram_prob = ngram.calculate_trigram_prob(list_of_trigrams, bigram_counts, trigram_counts)
    
    with open("input_csv_file_path.csv",'r') as csvinput:
        with open("output_csv_file_path.csv", 'w') as csvoutput:
            writer = csv.writer(csvoutput, lineterminator='\n')
            reader = csv.reader(csvinput)
            all = []
            row = next(reader)
            # print(row[0])
            row.append('NGLP')
            all.append(row)

            for row in reader:
                testcommit_name = "../data/MappingCommitFiles/" + row[0] + ".txt"
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

    print(" Finish !!! ")

   
