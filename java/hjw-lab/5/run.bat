@echo off
if '%1'=='1' goto st
:st
echo [5/8] ��ʼִ�е�5��ʵ�����
echo.
cd ./build
java Main
echo.
echo [5/8] ��5��ʵ�����ִ�����
echo.
cd ..
pause