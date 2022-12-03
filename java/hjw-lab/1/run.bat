@echo off
if '%1'=='1' goto st
:st
echo [1/8] 开始执行第1次实验程序
echo.
cd build
echo 执行程序1（判断能否构成直角三角形）：
echo.
java One
echo.
echo 执行完毕
echo.
pause
echo 执行程序2（判断输入的是几位数）：
echo.
java Two
echo.
echo 执行完毕
echo.
pause
echo 执行程序3（铁路运费计算）：
echo.
java Three
echo.
echo 执行完毕
echo.
pause
echo 执行程序4（1-1000偶数之和）：
echo.
java Four
echo.
echo 执行完毕
echo.
pause
echo 执行程序5（阶乘之和）：
echo.
java Five
echo.
echo 执行完毕
echo.
echo [1/8] 第1次实验程序执行完毕
echo.
cd ..
pause