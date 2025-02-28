rem 最後以外の項目はいじらないでください。
@echo off
chcp 65001 >nul

setlocal enabledelayedexpansion
cd /d "%~dp0CreateTodaysFile"
java -Dfile.encoding=UTF-8 -jar CreateTodaysFile.jar %1

rem 処理終了後自動的にコマンドプロンプトを閉じたい場合は以下を削除してください
pause:
