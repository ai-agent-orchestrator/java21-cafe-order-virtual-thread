@echo off
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
echo === JAVA 21 CAFE ORDER UTF-8 RUN ===
call gradlew.bat run
pause
