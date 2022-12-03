@echo off
if '%1'=='1' goto st
:st
echo [2/8] 正在编译第2次实验...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [2/8] 编译完成
echo.
if '%2'=='1' goto ed
pause
:ed