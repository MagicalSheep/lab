@echo off
if '%1'=='1' goto st
:st
echo [6/8] ��ʼִ�е�6��ʵ�����
echo.
cd ./build
echo ִ�г���1������3���̣߳���
echo.
java One
echo.
echo ִ�����
echo.
pause
echo ִ�г���2��ʹ���̼߳��������뱻3����������
echo.
java Two
echo.
echo ִ�����
echo.
echo [6/8] ��6��ʵ�����ִ�����
echo.
cd ..
pause