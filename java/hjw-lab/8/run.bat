@echo off
if '%1'=='1' goto st
:st
echo [8/8] 开始执行第8次实验程序
echo.
cd build
echo 执行程序1（计算[2, 200]内的素数并保存）：
echo.
java One
echo.
echo 执行完毕
echo.
pause
echo 执行程序2（寻找文件CONFIG.SYS）：
echo.
java Two
echo.
echo 执行完毕
echo.
pause
echo 执行程序3（序列化学生信息于文件中）：
echo.
java Three
echo.
echo 执行完毕
echo.
pause
echo 执行程序4（反序列化学生信息并统计）：
echo.
java Four
echo.
echo 执行完毕
echo.
echo [8/8] 第8次实验程序执行完毕
echo.
cd ..
pause