@echo off
if '%1'=='1' goto st
:st
echo [4/8] 开始执行第4次实验程序
echo.
cd ./build
java Main
echo.
echo [4/8] 第4次实验程序执行完毕
echo.
cd ..
pause