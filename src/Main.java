import Util.CreateFileUtils;
import Util.CreateTaskScheduler;
import Util.ImplementCreateFileUtils;
import Util.ImplementTaskSchedulerUtils;
import exception.MyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 万が一何か損失や損害が発生した場合、当方一切責任を負いません。
 * <p>
 * 改変等はご自由にしていただいて構いません。(LGPL)
 *
 * @author raindazo
 */

public class Main {
    static final String PROPERTY_FROM_FILE = "./VRCpbd.setting";
    static final String CREATE_TASK_SCHEDULER = "CreateTaskScheduler";
    static final String EMPTY = "";

    public static void main(String[] args) {
        String parameter = (args.length > 0 && !args[0].isEmpty()) ? args[0] : EMPTY;
        switch (parameter) {
            case CREATE_TASK_SCHEDULER -> CreateTaskSchedulerExecute(args);
            case EMPTY -> new SettingGUI();
        }
        FileCreateExecute(getPropertyFromFile());
    }

    /**
     * 日付分け実行
     *
     * @param args 仕分け対象ファイル・仕分け先ファイル
     * @author raindazo
     */
    public static void FileCreateExecute(String[] args) {
        System.out.println("処理を開始します。");

        CreateFileUtils implementCreateFile = new ImplementCreateFileUtils();

        //バリデーションチェック
        boolean validation = implementCreateFile.validation(args);

        if (validation) {
            return;
        }
        
        implementCreateFile.fileCreate(args[0], args[1]);

        //ファイル作成
//        if (!validation && implementCreateFile.fileCreate(args[0], args[1])) {
//            //移動ファイルが存在するか確認
//            implementCreateFile.checkMovedLogFile(args[1], args[2]);
//        }

        System.out.println("処理を終了しました。");
    }

    /**
     * 　タスクスケジューラ作成
     *
     * @param args 仕分け対象ファイル・仕分け先ファイル
     * @author raindazo
     */
    public static void CreateTaskSchedulerExecute(String[] args) {

        CreateTaskScheduler ImplementTaskScheduler = new ImplementTaskSchedulerUtils();

        ImplementTaskScheduler.readXml(args[1]);

    }

    /**
     * 設定ファイル読み込み
     *
     * @return 設定ファイルで読み取った値
     * @author raindazo
     */
    public static String[] getPropertyFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PROPERTY_FROM_FILE))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            new MyException(e);
            return new String[0];
        }
    }
}
