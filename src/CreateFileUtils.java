import java.io.File;
import java.util.List;

public interface CreateFileUtils {

    /**ファイルを作成*/
    void fileCreate(String path,String need);

    /**削除したファイルをlogファイルに出力*/
    void fillOutDeleteLog();

    /**DeleteLogファイルが存在するか確認*/
    void checkDeleteLogFile(String path,String need);

    /**写真が保存されて日付を確認*/
    String checkDate(File file);

    /**メッセージと例外を表示*/
    void exceptionLog(Exception exception);

    /**写真を日付分け*/
    void movePictures(List<File> picturesList,String createPictureFilePath);

    /**移動した写真を削除する*/
    void pictureDelete(String deletePicture);

}