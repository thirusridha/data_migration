echo ** Running DataMigrationa and Report service **.
echo ** Process started Please wait......
dt=$(date '+%d-%m-%Y-%H%M%S');
echo $1 
cd ..
java -jar ./lib/DataMigrationProgram-0.0.1-SNAPSHOT.jar datamigration $1 >./logs/DataMigration_Script_log_$dt.txt
echo ** Process completed**
cat ./logs/DataMigration_Script_log_$dt.txt
