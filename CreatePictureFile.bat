rem 最後以外の項目はいじらないでください。
@echo off
chcp 65001

cd ./CreateTodaysFile
java -jar CreateTodaysFile.jar %1

rem 処理終了後自動的にコマンドプロンプトを閉じたい場合は以下を削除してください
pause:
