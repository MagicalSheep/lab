@echo off
if '%1'=='1' goto st
:st
echo [7/8] ��ʼִ�е�7��ʵ�����
echo.
cd build
echo ִ�г���1����Сд��ĸ��ת����
echo.
java One
echo.
echo ִ�����
echo.
pause
echo ִ�г���2���жϻ��Ĵ�����
echo.
java Two
echo.
echo ִ�����
echo.
pause
echo ִ�г���3��ɾ���Ӵ�����
echo.
java Three
echo.
echo ִ�����
echo.
pause
echo ִ�г���4��ɾ���ظ��ַ�����
echo.
java Four
echo.
echo ִ�����
echo.
pause
echo ִ�г���5��ͳ���ַ�������Ƶ�ʣ���
echo.
java Five
echo.
echo ִ�����
echo.
pause
echo ִ�г���6�����ŷָ��ַ�������
echo.
java Six
echo.
echo ִ�����
echo.
echo [7/8] ��7��ʵ�����ִ�����
echo.
cd ..
pause