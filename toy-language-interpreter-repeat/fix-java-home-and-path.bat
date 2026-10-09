@echo off
setlocal
cd /d "%~dp0"
where mvn >nul 2>&1
if errorlevel 1 (
  echo Install Maven and a JDK, then add Maven to PATH.
  exit /b 1
)
call mvn javafx:run
exit /b %ERRORLEVEL%
