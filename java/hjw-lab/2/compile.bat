@echo off
if '%1'=='1' goto st
:st
echo [2/8] ���ڱ����2��ʵ��...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [2/8] �������
echo.
if '%2'=='1' goto ed
pause
:ed