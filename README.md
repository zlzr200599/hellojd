![image](https://github.com/lexsaints/powershell/blob/master/IMG/ps2.png)
# 工程简介

# 延伸阅读

## init the dataset

first go into the preUtils dir run:

$bash preprocess.sh

after the exec of preprocess.sh, you will see a dir named dataset. copy the dataset dir to the resource dir

$mv dataset ../src/main/resource/

## run the packaged project

$java -jar  target/hellojd-0.0.1-SNAPSHOT.jar
