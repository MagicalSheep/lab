@echo off
if '%1'=='1' goto st
:st
echo [3/8] ���ڱ����3��ʵ��...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [3/8] �������
echo.
if '%2'=='1' goto ed
pause
:ed