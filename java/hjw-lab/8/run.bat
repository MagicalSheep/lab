@echo off
if '%1'=='1' goto st
:st
echo [8/8] ��ʼִ�е�8��ʵ�����
echo.
cd build
echo ִ�г���1������[2, 200]�ڵ����������棩��
echo.
java One
echo.
echo ִ�����
echo.
pause
echo ִ�г���2��Ѱ���ļ�CONFIG.SYS����
echo.
java Two
echo.
echo ִ�����
echo.
pause
echo ִ�г���3�����л�ѧ����Ϣ���ļ��У���
echo.
java Three
echo.
echo ִ�����
echo.
pause
echo ִ�г���4�������л�ѧ����Ϣ��ͳ�ƣ���
echo.
java Four
echo.
echo ִ�����
echo.
echo [8/8] ��8��ʵ�����ִ�����
echo.
cd ..
pause