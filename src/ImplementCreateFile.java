import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ImplementCreateFile implements CreateFileUtils {

    /**
     * @param vrcPictureFilePath    VRCの写真が格納されているパス
     * @param createPictureFilePath 撮影日分けされたファイルを保存するパス
     *                              <p>ファイルを作成 </p>
     */
    @Override
    public void fileCreate(String vrcPictureFilePath, String createPictureFilePath) {
        File[] dirs = new File(vrcPictureFilePath).listFiles();

        if (dirs != null) {
            List<File> picturesList = new ArrayList<>();

            Arrays.stream(dirs)
                    .filter(file -> !file.toString().equals(createPictureFilePath))
                    .forEach(file -> {
                        picturesList.addAll(Arrays.asList(Objects.requireNonNull(new File(String.valueOf(file)).listFiles())));
                    });

            Set<Path> datelList = new LinkedHashSet<>();

            picturesList.stream()
                    .map(this::checkDate)
                    .forEach(value -> datelList.add(Paths.get(createPictureFilePath + "\\" + value)));

            List<File> existFile = Arrays.stream(Objects.requireNonNull(new File(createPictureFilePath).listFiles())).toList();

            //出力ファイルにすでに日付ファイルがあれば生成リストから削除
            existFile.stream().map(String::valueOf).forEach(value -> datelList.remove(Paths.get(value)));

            datelList.forEach(d -> {
                try {
                    Files.createDirectory(d);
                } catch (IOException e) {
                    exceptionLog(e);
                }
            });

            DeleteLog deleteLog = new DeleteLog();
            deleteLog.setDeletelList(picturesList);
        }
    }

    /**
     * 削除したファイルをlogファイルに出力
     */
    @Override
    public void fillOutDeleteLog() {
        final String today = LocalDate.now().toString().replace("-", "");
        DeleteLog deleteLog = new DeleteLog();

        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("./DeleteLog/" + today + "_DeleteLog.txt"));

            for (File value : deleteLog.getDeletelList()) {
                bw.write(value.toString());
            }
            bw.close();

            System.out.println("削除したファイルをlogファイルに出力しました。[" + bw + "]");
        } catch (IOException e) {
            exceptionLog(e);
        }


    }

    /**
     * DeleteLogファイルが存在するか確認
     */
    @Override
    public void checkDeleteLogFile(String path, String need) {
        try {
            if (need.equals("True")) {
                File[] dir = new File(path).listFiles();
                boolean existDeleteLogFile = false;

                for (File f : Objects.requireNonNull(dir)) {
                    if (f.getName().equals(path + "\\DeleteLog")) {
                        existDeleteLogFile = true;
                        break;
                    }
                }

                if (!existDeleteLogFile) {
                    Files.createDirectory(Path.of(path + "\\DeleteLog"));
                    System.out.println("DeleteLogファイルを作成しました。");
                }
            }
        } catch (IOException e) {
            exceptionLog(e);
            System.exit(0);
        }
    }

    /**
     * 写真が保存されて日付を確認
     *
     * @param file 日付を取得したいファイルパス
     */
    @Override
    public String checkDate(File file) {
        BasicFileAttributes attrs;
        String pictureDate = null;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.creationTime();

            pictureDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date(time.toMillis()));
        } catch (IOException e) {
            exceptionLog(e);
        }
        return pictureDate;
    }

    /**
     * メッセージと例外を表示
     */
    @Override
    public void exceptionLog(Exception e) {
        System.out.println("例外が発生しました。");
        System.out.println("解決できない場合は、お手数おかけしますが、以下のログと合わせて開発者にご連絡ください。");
        e.printStackTrace();
    }

    /**
     * 写真を日付分けする
     */
    @Override
    public void movePictures() {

    }

    /**
     * 移動した写真を削除する
     */
    @Override
    public void pictureDelete(String deletePicture) {
        if (deletePicture.equals("True")) {
            DeleteLog deleteLog = new DeleteLog();

            deleteLog.getDeletelList().forEach(System.out::println);

            fillOutDeleteLog();
        }
    }
}