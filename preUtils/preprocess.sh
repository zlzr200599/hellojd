mkdir utf8_files
unzip -O GBK 时效数据.zip
find . -maxdepth 1  -type f -name "*.csv"  -exec iconv -f GBK -t UTF-8 {} -o utf8_files/{} \;

awk '(NR == 1) || (FNR > 1)' utf8_files/揽收时效数_{1,2,3}.csv > collect.csv
awk '(NR == 1) || (FNR > 1)' utf8_files/中转时效数据_{1,2,3}.csv > route.csv
awk '(NR == 1) || (FNR > 1)' utf8_files/派送时效数据_{1,2,3}.csv > dispatch.csv

python3 csv2json.py
mkdir dataset
mv collect2bitmap.json dataset/collect2bitmap.json
mv route2bitmap.json dataset/route2bitmap.json
mv dispatch2bitmap.json dataset/dispatch2bitmap.json

echo "preprocess complete"

