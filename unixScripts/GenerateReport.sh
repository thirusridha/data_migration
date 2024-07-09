dt=$(date '+%d-%m-%Y-%H%M%S');
echo ** Running DataMigrationa and Report service **
cd ..
echo ** Process started Please wait......
java -jar ./lib/DataMigrationProgram-0.0.1-SNAPSHOT.jar $1 $2 $3 $4 > ./logs/Report_Log_$dt.txt
cat ./logs/Report_Log_$dt.txt
echo ** Process completed**
