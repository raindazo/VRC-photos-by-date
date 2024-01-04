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

public class ImplementCreateFileUtils implements CreateFileUtils {

    /**
     * @param vrcPictureFilePath    VRCの写真が格納されているパス
     * @param createPictureFilePath 撮影日分けされたファイルを保存するパス
     *                              <p>ファイルを作成 </p>
     * @author raindazo
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
            movePictures(picturesList, createPictureFilePath);
        }
    }

    /**
     * 移動したファイルをlogファイルに出力
     *
     * @author raindazo
     */
    @Override
    public void fillmovedLog(Path path) {
        final String today = LocalDate.now().toString().replace("-", "");

        try {
            String fileName = path + "\\" + today + "_MovedLog.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,true));
            for (String value : MovedLog.getList()) {
                bw.write(value);
                bw.newLine();
            }
            bw.close();

            System.out.println("移動したファイルをlogファイルに出力しました。[" + fileName + "]");
        } catch (IOException e) {
            exceptionLog(e);
        }


    }

    /**
     * MovedLogファイルが存在するか確認
     *
     * @author raindazo
     */
    @Override
    public void checkDeleteLogFile(String path, String need) {
        try {
            if (need.equals("True")) {
                File[] dir = new File(path).listFiles();
                boolean existDeleteLogFile = Arrays.stream(Objects.requireNonNull(dir))
                        .anyMatch(f -> f.getName().equals("MovedLog"));

                Path movedLogPath = Path.of(path + "\\MovedLog");

                if (!existDeleteLogFile) {
                    Files.createDirectory(movedLogPath);
                    System.out.println("DeleteLogファイルを作成しました。");
                }
                fillmovedLog(movedLogPath);
            }
        } catch (IOException e) {
            exceptionLog(e);
        }
    }

    /**
     * 写真が保存されて日付を確認
     *
     * @param file 日付を取得したいファイルパス
     * @author raindazo
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
     *
     * @author raindazo
     */
    @Override
    public void exceptionLog(Exception e) {
        System.out.println("例外が発生しました。");
        System.out.println("解決できない場合は、お手数おかけしますが、以下のログと合わせて開発者にご連絡ください。");
        e.printStackTrace();
        System.exit(0);
    }

    /**
     * 写真を日付分けする
     *
     * @author raindazo
     */
    @Override
    public void movePictures(List<File> picturesList, String createPictureFilePath) {
        File[] datePath = new File(createPictureFilePath).listFiles();

        for (File picture : picturesList) {
            for (File path : Objects.requireNonNull(datePath)) {
                if (checkDate(picture).equals(path.getName())) {
                    MovedLog.addList(picture.getName());
                    picture.renameTo(new File(path + "\\" + picture.getName()));
                    break;
                }
            }
        }
    }
}