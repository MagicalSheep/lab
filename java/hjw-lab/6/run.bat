@echo off
if '%1'=='1' goto st
:st
echo [6/8] 开始执行第6次实验程序
echo.
cd ./build
echo 执行程序1（创建3条线程）：
echo.
java One
echo.
echo 执行完毕
echo.
pause
echo 执行程序2（使用线程计算素数与被3整除数）：
echo.
java Two
echo.
echo 执行完毕
echo.
echo [6/8] 第6次实验程序执行完毕
echo.
cd ..
pause