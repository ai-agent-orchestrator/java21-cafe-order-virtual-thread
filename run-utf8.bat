@echo off
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
echo === ORIGINAL VERSION UTF-8 RUN ===
if exist out rmdir /s /q out
javac -encoding UTF-8 -d out src\main\java\com\assignment\cafe\*.java src\main\java\com\assignment\cafe\controller\*.java src\main\java\com\assignment\cafe\exception\*.java src\main\java\com\assignment\cafe\model\*.java src\main\java\com\assignment\cafe\repository\*.java src\main\java\com\assignment\cafe\service\*.java src\main\java\com\assignment\cafe\view\*.java
if errorlevel 1 (
    echo 컴파일 실패
    pause
    exit /b 1
)
java -cp out com.assignment.cafe.CafeOrderApplication
pause
