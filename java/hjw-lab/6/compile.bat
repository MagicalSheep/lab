@echo off
if '%1'=='1' goto st
:st
echo [6/8] ���ڱ����6��ʵ��...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [6/8] �������
echo.
if '%2'=='1' goto ed
pause
:ed