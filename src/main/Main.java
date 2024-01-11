package main;

import Util.CreateFileUtils;
import Util.CreateTaskScheduler;
import Util.ImplementCreateFileUtils;
import Util.ImplementTaskSchedulerUtils;

/**
 * 万が一何か損失や損害が発生した場合、当方一切責任を負いません。
 * <p>
 * 改変等はご自由にしていただいて構いません。(LGPL)
 *
 * @author raindazo
 */

class Main {
    public static void main(String[] args) {
        if (args[0].equals("CreateTaskScheduler")) {
            CreateTaskSchedulerExecute(args);
        } else {
            FileCreateExecute(args);
        }
    }

    public static void FileCreateExecute(String[] args) {
        System.out.println("処理を開始します。");

        CreateFileUtils implementCreateFile = new ImplementCreateFileUtils();

        //バリデーションチェック
        boolean validation = implementCreateFile.validation(args);

        //ファイル作成
        if (!validation && implementCreateFile.fileCreate(args[0], args[1])) {
            //移動ファイルが存在するか確認
            implementCreateFile.checkMovedLogFile(args[1], args[2]);
        }

        System.out.println("処理を終了しました。");
    }

    public static void CreateTaskSchedulerExecute(String[] args) {

        CreateTaskScheduler ImplementTaskScheduler = new ImplementTaskSchedulerUtils();

        ImplementTaskScheduler.readXml(args[1]);


    }
}
