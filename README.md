# 工程简介
![image](https://img30.360buyimg.com/img/jfs/t1/190494/25/7165/857441/60bf4124E2795b317/7eaeebfc57b53e77.jpg)

# 主要方法

## init the dataset

first go into the preUtils dir run:

$bash preprocess.sh

after the exec of preprocess.sh, you will see a dir named dataset. copy the dataset dir to the resource dir

$mv dataset ../src/main/resource/

## run the packaged project

$java -jar  target/hellojd-0.0.1-SNAPSHOT.jar
