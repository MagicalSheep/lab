@echo off
if '%1'=='1' goto st
:st
echo [3/8] 开始执行第3次实验程序
echo.
cd ./build
java Main
echo.
echo [3/8] 第3次实验程序执行完毕
echo.
cd ..
pause