rem 以下の2行はいじらないでください。
@echo off
chcp 65001

rem イコール(=)の右に指定されている値を入力してください

rem VRCの写真が格納されているパス(例：C:\Users\user\画像\VRChat (yyyy-MMが保存されているファイルです))
set vrcPictureFilePath=C:\Users\rein6\OneDrive\画像\VRChat

rem 撮影日分けされたファイルを保存するパス(例：C:\Users\output\画像\VRChat)
set createPictureFilePath=C:\Users\rein6\OneDrive\画像\VRChat\日付分け

rem 写真の名前をログに記録するか(記録する：True , 記録しない：False)
set showDeletedPictureName=True


rem 以下の2行はいじらないでください。
cd ./CreateTodaysFile
java -jar CreateTodaysFile.jar %vrcPictureFilePath% %createPictureFilePath% %showDeletedPictureName%


rem 処理終了後自動的にコマンドプロンプトを閉じたい場合は以下を削除してください
pause: