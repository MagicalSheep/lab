@echo off
if '%1'=='1' goto st
:st
echo [4/8] ��ʼִ�е�4��ʵ�����
echo.
cd ./build
java Main
echo.
echo [4/8] ��4��ʵ�����ִ�����
echo.
cd ..
pause