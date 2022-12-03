@echo off
if '%1'=='1' goto st
:st
echo [7/8] 开始执行第7次实验程序
echo.
cd build
echo 执行程序1（大小写字母互转）：
echo.
java One
echo.
echo 执行完毕
echo.
pause
echo 执行程序2（判断回文串）：
echo.
java Two
echo.
echo 执行完毕
echo.
pause
echo 执行程序3（删除子串）：
echo.
java Three
echo.
echo 执行完毕
echo.
pause
echo 执行程序4（删除重复字符）：
echo.
java Four
echo.
echo 执行完毕
echo.
pause
echo 执行程序5（统计字符串出现频率）：
echo.
java Five
echo.
echo 执行完毕
echo.
pause
echo 执行程序6（逗号分隔字符串）：
echo.
java Six
echo.
echo 执行完毕
echo.
echo [7/8] 第7次实验程序执行完毕
echo.
cd ..
pause