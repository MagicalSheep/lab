@echo off
if '%1'=='1' goto st
:st
echo [1/8] ��ʼִ�е�1��ʵ�����
echo.
cd build
echo ִ�г���1���ж��ܷ񹹳�ֱ�������Σ���
echo.
java One
echo.
echo ִ�����
echo.
pause
echo ִ�г���2���ж�������Ǽ�λ������
echo.
java Two
echo.
echo ִ�����
echo.
pause
echo ִ�г���3����·�˷Ѽ��㣩��
echo.
java Three
echo.
echo ִ�����
echo.
pause
echo ִ�г���4��1-1000ż��֮�ͣ���
echo.
java Four
echo.
echo ִ�����
echo.
pause
echo ִ�г���5���׳�֮�ͣ���
echo.
java Five
echo.
echo ִ�����
echo.
echo [1/8] ��1��ʵ�����ִ�����
echo.
cd ..
pause