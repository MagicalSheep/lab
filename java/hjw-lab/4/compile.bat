@echo off
if '%1'=='1' goto st
:st
echo [4/8] ���ڱ����4��ʵ��...
javac -encoding utf-8 -d ./build/ ./code/*.java
echo [4/8] �������
echo.
if '%2'=='1' goto ed
pause
:ed