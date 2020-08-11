# EJTool

EJTool is a tool which build a defect prediction model using WEKA. The input is arff files, and output is a csv file that shows the performance of the model measured by various measurements such as precision, recall, f-measure, AUC. In addition, EJTool provides N-gram Log Probability (NGLP) metric values.

## Manual script steps

1. Create N-gram training data and get the NGLP metric: You can use [BugPatchCollector](https://github.com/HGUISEL/bugpatchcollector) tool to make one text file with all the added lines of a project's commit. You have to replace the text file to `EJTool/Ngram/data/AllCommitAddLines.txt`. In addition, you extract metrics and commits from your project using  [BugPatchCollector](https://github.com/HGUISEL/bugpatchcollector) tool. After extracting commits, you add the files under `EJTool/Ngram/data/MappingCommitFiles` directory. When `AllCommitAddLines.txt`, `MappingCommitFiles` directory and the metrics csv file are ready, you will run `EJTool/Ngram/src/code_ngram.py` to get NGLP metric value. 
2. Change NGLP metrics csv file to arff file using  `EJTool/src/main/java/edu/handong/csee/isel/weka/CSV2Arff.java`
3. Save a cross-validation dataset using  `EJTool/src/main/java/edu/handong/csee/isel/weka/SaveCrossValidation.java` 
4. Run the EJTool executable file: Before you run the EJTool, you have to change a `searchDirPath` variable in a main method of `[SimpleThreadPool.java](http://simplethreadpool.java)` to a path of a directory containing the original dataset.  

## How to build: Gradle

```
$ ./gradlew distZip
```

or

```
$ gradle distZip
```

After the command do follwing to unzip the distZip:

```
unzip /build/distributions/EJTool.zip
```

The executable file is in build/distributions/EJTool/bin

If you have trouble to build using gradlew, enter

```
$ gradle wrap
```

## Options

> Required options

- -s (source) source arff file path.
- -d (dataUnbalancingMode) "1" is noHandling data unbalance or "2" is applying spread  subsampling or "3" is applying smote algorithm.
- -t (type) "1" is a original dataset or applying PCA or VIF to remove multicollinearity or "2" is applying Correlation-based feature selection or "3" is applying Wrapper-based feature selection.
- -c (csv) file path of output to output file.
- -m (model) machine learning model.
- -i (iter) number of k-fold cross-validation iterations
- -f (fold) the number of cross-validation folds
- -p (pool) thread pool size for multi-thread

## **Example Tests**

```
./EJTool -s /home/eunjiwon/Git/EJTool/cross_validation_data/ -t 1 -c /home/eunjiwon/Git/EJTool/results/nglp_DT_result.csv -i 10 -f 10 -m weka.classifiers.trees.J48 -d 3 -p 15
```
