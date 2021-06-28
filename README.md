# 工程简介
![image](https://img30.360buyimg.com/img/jfs/t1/190494/25/7165/857441/60bf4124E2795b317/7eaeebfc57b53e77.jpg)

# 主要方法

## init the dataset

first go into the preUtils dir run:

$bash preprocess.sh

after the exec of preprocess.sh, you will see a dir named dataset. copy the dataset dir to the resource dir

$mv dataset ../src/main/resource/

## install & package into jar(No Need, there's a jar already.)

cd /path_to_your_dir/hellojd

mvn clean install

# TL;DR  =>  run the packaged project

$java -jar  /path_to_your_dir/hellojd/target/hellojd-0.0.1-SNAPSHOT.jar

# API
your_app_server_ip:8080/{始发地址编码}/{目的地址编码}/{下单时间戳}

始发/目的地址从小地址到大地址排列，从乡镇:县区:地市:省排列，可能出现小地址缺失，则不写该级别地址。用:间隔，如:

http://39.105.195.204:8080/59784501:131820:82056:909/92682:1690146:18/1621385821   

39.105.195.204 为我的服务器ip

59784501:131820:82056:909  为始发地  四季青街道:西固区:兰州市:甘肃

92682:1690146:18 为目的地   六环外（马驹桥镇）:通州区:北京   这里目的地址只使用了三级地址。

1621385821 为下单时间2021-5-19-08:57:01的时间戳戳

获得有效返回值: 2021-05-22 22:00:00

若无有效值，返回-1

