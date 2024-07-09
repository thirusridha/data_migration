@echo off
set processtype=%1
set resportformat=%2
set reportName=%3
set givenDate=%4
set _date=%DATE%-%TIME%
set _date=%_date:/=-%
set _date=%_date: =-%
set _date=%_date::=%
set _date=%_date:.=%

echo ** Running DataMigrationa and Report service **
cd ..
echo ** Process started Please wait......

java -jar .\lib\DataMigrationProgram-0.0.1-SNAPSHOT.jar %processtype% %resportformat% %reportName% %givenDate% 2>&1 > .\logs\Report_Log_%_date%.txt && type .\logs\Report_Log_%_date%.txt

echo ** Process completed**

pause
EXIT