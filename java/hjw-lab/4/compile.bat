@echo off
if '%1'=='1' goto st
:st
echo [4/8] 正在编译第4次实验...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [4/8] 编译完成
echo.
if '%2'=='1' goto ed
pause
:ed