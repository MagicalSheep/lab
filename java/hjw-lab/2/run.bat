@echo off
if '%1'=='1' goto st
:st
echo [2/8] ��ʼִ�е�2��ʵ�����
echo.
cd build
java Main
echo.
echo [2/8] ��2��ʵ�����ִ�����
echo.
cd ..
pause