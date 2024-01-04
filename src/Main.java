/**
 * あくまでも自分の環境で自分のために作ったプログラムです。
 * 万が一何か損失や損害が発生した場合、当方一切責任を負いません。
 * <p>
 * 改変等はご自由にしていただいて構いません。(LGPL)
 *
 * @author raindazo
 */

class Main {
    public static void main(String[] args) {

        System.out.println("処理を開始します。");

        ImplementCreateFileUtils implementCreateFile = new ImplementCreateFileUtils();

        boolean validation = implementCreateFile.validation(args);

        //ファイル作成
        if (!validation && implementCreateFile.fileCreate(args[0], args[1])) {
            //移動ファイルが存在するか確認
            implementCreateFile.checkDeleteLogFile(args[1], args[2]);
        }

        System.out.println("処理が終了しました。");
    }
}
