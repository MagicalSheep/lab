@echo off
if '%1'=='1' goto st
:st
echo [7/8] 正在编译第7次实验...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [7/8] 编译完成
echo.
if '%2'=='1' goto ed
pause
:ed