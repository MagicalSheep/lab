@echo off
if '%1'=='1' goto st
:st
echo [3/8] ��ʼִ�е�3��ʵ�����
echo.
cd ./build
java Main
echo.
echo [3/8] ��3��ʵ�����ִ�����
echo.
cd ..
pause