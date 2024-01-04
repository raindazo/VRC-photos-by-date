/**
 * あくまでも自分の環境で自分のために作ったプログラムです。
 * 万が一何か損失や損害が発生した場合、当方一切責任を負いません。
 * <p>
 * 改変等はご自由にしていただいて構いません。(LGPL)
 *
 * @author raindazo
 */

class Main {
    private static final String ng = ".*[@＠｢「｣」：]+.*";

    public static void main(String[] args) {

        System.out.println("処理を開始します。");

        ImplementCreateFileUtils implementCreateFile = new ImplementCreateFileUtils();

        if (args[0].matches(ng) || args[1].matches(ng)) {
            System.out.println(args[0] + "もしくは" + args[1] + "に使用できない文字(￥,：,＊,？,”,＜,＞,｜)が含まれています。");
            System.out.println("パスを確認後、再度起動してください。");
            return;
        } else if (args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty()) {
            System.out.println("入力されていない項目があります。");
            return;
        }

        //ファイル作成
        implementCreateFile.fileCreate(args[0], args[1]);

        //エラーログファイルが存在するか確認
        implementCreateFile.checkDeleteLogFile(args[1], args[3]);

        System.out.println("処理が終了しました。");
    }
}
