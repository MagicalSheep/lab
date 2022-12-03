@echo off
if '%1'=='1' goto st
:st
echo [2/8] 开始执行第2次实验程序
echo.
cd build
java Main
echo.
echo [2/8] 第2次实验程序执行完毕
echo.
cd ..
pause