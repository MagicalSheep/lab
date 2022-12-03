@echo off
if '%1'=='1' goto st
:st
echo [8/8] 正在编译第8次实验...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [8/8] 编译完成
echo.
if '%2'=='1' goto ed
pause
:ed