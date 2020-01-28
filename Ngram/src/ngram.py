import math
import random

class Ngram:
    def __init__(self, N):
        self.N = N
        self.onegram_prob = None
        self.bigram_prob = None
        self.trigram_prob = None

    def train(self, sentences):
        listOfBigrams = []
        bigramCounts = {}
        unigramCounts = {}

        listOfTrigrams = []
        trigramCounts = {}

        for sentence in sentences:
            words = sentence
            for i in range(len(words)):
                if i < len(words) - 1:
                    listOfBigrams.append((words[i], words[i + 1]))
                    if (words[i], words[i + 1]) in bigramCounts:
                        bigramCounts[(words[i], words[i + 1])] += 1
                    else:
                        bigramCounts[(words[i], words[i + 1])] = 1

                    if i < len(words) - 2:
                        listOfTrigrams.append((words[i], words[i + 1], words[i + 2]))
                        if (words[i], words[i + 1], words[i + 2]) in trigramCounts:
                            trigramCounts[(words[i], words[i + 1], words[i + 2])] += 1
                        else:
                            trigramCounts[(words[i], words[i + 1], words[i + 2])] = 1

                if words[i] in unigramCounts:
                    unigramCounts[words[i]] += 1
                else:
                    unigramCounts[words[i]] = 1

        return listOfBigrams, unigramCounts, bigramCounts, listOfTrigrams, trigramCounts
    
    def prob(self, sentence):
        '''Returns the MLE probability of the given sentence. '''
        if self.N == 3:
            return self.get_trigram_prob(sentence)
        elif self.N == 2:
            return self.get_bigram_prob(sentence)
        elif self.N == 1:
            return self.get_onegram_prob(sentence)

    def get_onegram_prob(self, sentence):
        '''Return onegram probabilty'''
        score = 1
        for word in sentence.split():
            score *= self.onegram_prob.get(word, 0)
        return score

    def calculate_onegram_prob(self, unigram_counts):
        '''Assigns onegram probabilties'''
        onegram_prob = {}
        total_word_count = 0
        for elem in unigram_counts:
            total_word_count += unigram_counts[elem]

        for onegram in unigram_counts:
            onegram_prob[onegram] = (unigram_counts.get(onegram)) / total_word_count

        with open('../output/1gram.txt', 'w') as file:
            file.write('Onegram' + '\t\t\t' + 'Count' + '\t' + 'Probability' + '\n')
            for unigram in unigram_counts:
                file.write(str(unigram) + ' : ' + str(unigram_counts[unigram])
                           + ' : ' + str(onegram_prob[unigram]) + '\n')
        self.onegram_prob = onegram_prob
        return onegram_prob

    def get_bigram_prob(self, sentence):
        '''Return bigram probabilty of the sentence'''
        score = 1
        words = sentence.split()
        for i in range(len(words) - 1):
            unit = (words[i], words[i + 1])
            if unit in self.bigram_prob:
                score *= self.bigram_prob[unit]
            else:
                score *= 0
        return score

    def calculate_bigram_prob(self, listOfBigrams, unigramCounts, bigramCounts):
        '''Assigns bigram probabilties'''
        bigram_prob = {}
        for bigram in listOfBigrams:
            bigram_prob[bigram] = (bigramCounts.get(bigram)) / (unigramCounts.get(bigram[0]))

        with open('../output/2gram.txt', 'w') as file:
            file.write('Bigram' + '\t\t\t' + 'Count' + '\t' + 'Probability' + '\n')
            for bigrams in listOfBigrams:
                file.write(str(bigrams) + ' : ' + str(bigramCounts[bigrams])
                           + ' : ' + str(bigram_prob[bigrams]) + '\n')
        self.bigram_prob = bigram_prob
        return bigram_prob

    def get_trigram_prob(self, sentence):
        '''Returns trigram probabilty of the sentence'''
        score = 1
        words = sentence.split()
        for i in range(len(words) - 2):
            unit = (words[i], words[i + 1], words[i + 2])
            if unit in self.trigram_prob:
                score *= self.trigram_prob[unit]
            else:
                score *= 0
        return score

    def calculate_trigram_prob(self, list_of_trigrams, bigram_counts, trigram_counts):
        '''Assigns trigram probabilties'''
        trigram_prob = {}
        for trigram in list_of_trigrams:
            trigram_prob[trigram] = (trigram_counts.get(trigram)) / (bigram_counts.get((trigram[0], trigram[1])))

        with open('../output/3gram.txt', 'w') as file:
            file.write('Trigram' + '\t\t\t' + 'Count' + '\t' + 'Probability' + '\n')
            for bigrams in list_of_trigrams:
                file.write(str(bigrams) + ' : ' + str(trigram_counts[bigrams])
                           + ' : ' + str(trigram_prob[bigrams]) + '\n')
        self.trigram_prob = trigram_prob
        return trigram_prob

    def trigram_NGLP(self, sentences):
        '''Returns the trigram NGLP of the given list of sentences. Here smoothed values are used.'''
        # counter = 0
        tmp = 0
        for i in range(len(sentences)):
            sentence = sentences[i]
            for j in range(len(sentence) - 2):
                unit = (sentence[j], sentence[j + 1], sentence[j + 2])
                if unit in self.trigram_prob:
                    prob = self.trigram_prob[unit]
                else:
                    prob = 0
                if prob == 0:
                    tmp += 0
                else:
                    tmp += math.log(prob, 2)
        NGLP = tmp * -1
        return NGLP
