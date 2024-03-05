@echo off
chcp 65001
setlocal enabledelayedexpansion

echo 設定を開始します

set "taskPath=%~dp0CreatePictureFile.bat"
set "jarName=%~dp0CreateTodaysFile.jar"
set "originalFile=%~dp0original.xml"
set "outputFile=%~dp0modified.xml"

set TaskName="VRC写真移動"

schtasks /create /tn "%TaskName%" /tr "%taskPath%" /sc onlogon
timeout /t 1 /nobreak >nul
schtasks /query /tn "%TaskName%" /xml > "%originalFile%"
timeout /t 1 /nobreak >nul
schtasks /delete /tn "%TaskName%" /F
timeout /t 3 /nobreak >nul
java -jar "%jarName%" CreateTaskScheduler %originalFile%
timeout /t 1 /nobreak >nul
schtasks /CREATE /TN "%TaskName%" /XML "%outputFile%"
timeout /t 1 /nobreak >nul

del "%originalFile%"
del "%outputFile%"

echo 設定完了しました。

echo 5秒後に自動終了します。
timeout /t 5 /nobreak
exit