@echo off
if '%1'=='1' goto st
:st
echo [5/8] 开始执行第5次实验程序
echo.
cd ./build
java Main
echo.
echo [5/8] 第5次实验程序执行完毕
echo.
cd ..
pause