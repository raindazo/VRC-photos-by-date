package Util;

import exception.MyException;
import field.MovedLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImplementCreateFileUtils implements CreateFileUtils {

    /**
     * @param vrcPictureFilePath    VRCの写真が格納されているパス
     * @param createPictureFilePath 撮影日分けされたファイルを保存するパス
     *                              <p>ファイルを作成 </p>
     * @author raindazo
     */
    @Override
    public boolean fileCreate(String vrcPictureFilePath, String createPictureFilePath) {
        File[] pictureFilePath = new File(vrcPictureFilePath).listFiles();
        File[] createFilePath = new File(createPictureFilePath).listFiles();

        if (Objects.nonNull(pictureFilePath) && Objects.nonNull(createFilePath)) {
            List<File> separated = Stream.of(pictureFilePath).filter(file -> !file.isDirectory()).toList();

            //pictureFilePathにcreatePictureFilePathを含まないようにする。
            List<File> picturesList = Arrays.stream(pictureFilePath)
                    .filter(File::isDirectory)
                    .filter(file -> !file.equals(new File(createPictureFilePath)))
                    .flatMap(file -> Arrays.stream(Objects.requireNonNull(file.listFiles())))
                    .toList();

            List<File> margeList = Stream.concat(picturesList.stream(), separated.stream()).toList();

            //作成する必要がある日付ファイルのパスを取得
            Set<Path> dateList = margeList.stream()
                    .map(this::checkDate)
                    .map(value -> Paths.get(createPictureFilePath, value))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            //出力ファイルの中身を取得
            List<File> existFile = Arrays.stream(Objects.requireNonNull(new File(createPictureFilePath).listFiles())).toList();

            //出力ファイルにすでに日付ファイルがあれば生成リストから削除
            existFile.stream()
                    .map(String::valueOf)
                    .map(Paths::get)
                    .forEach(dateList::remove);

            //日付ファイルを作成
            dateList.forEach(d -> {
                try {
                    Files.createDirectory(d);
                } catch (IOException e) {
                    new MyException(e);
                }
            });
            //写真を日付分けする
            movePictures(margeList, createPictureFilePath);
        } else {
            String notPath = Objects.isNull(pictureFilePath) ? vrcPictureFilePath : createPictureFilePath;
            System.out.println(notPath + "が存在しません。\n存在するファイルを再度設定してください。");
            return false;
        }
        return true;
    }

    /**
     * 移動したファイルをlogファイルに出力
     *
     * @author raindazo
     */
    @Override
    public void fillmovedLog(Path path) {
        String today = LocalDate.now().toString().replace("-", "");

        if (!MovedLog.getList().isEmpty()) {
            try {
                Path logFilePath = Paths.get(path.toString(), today + "_MovedLog.txt");
                Files.write(logFilePath, MovedLog.getList(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("移動したファイルをlogファイルに出力しました。[" + logFilePath + "]");
            } catch (IOException e) {
                new MyException(e);
            }
        } else {
            System.out.println("移動したファイルはありませんでした。");
        }
    }

    /**
     * MovedLogファイルが存在するか確認
     *
     * @author raindazo
     */
    @Override
    public void checkMovedLogFile(String path, String need) {
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
            new MyException(e);
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
            FileTime time = attrs.lastModifiedTime();

            pictureDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date(time.toMillis()));
        } catch (IOException e) {
            new MyException(e);
        }
        return pictureDate;
    }

    /**
     * 写真を日付分けする
     *
     * @author raindazo
     */
    @Override
    public void movePictures(List<File> picturesList, String createPictureFilePath) {
        File[] datePath = new File(createPictureFilePath).listFiles();

        picturesList.forEach(picture -> Arrays.stream(Objects.requireNonNull(datePath))
                .filter(path -> checkDate(picture).equals(path.getName()))
                .findFirst()
                .ifPresent(path -> {
                    MovedLog.addEntry(picture.getName(), path.getName());
                    picture.renameTo(new File(path.toPath().resolve(picture.getName()).toString()));
                }));

        if (!MovedLog.getList().isEmpty()) {
            System.out.println(MovedLog.getList().size() + "件ファイルを移動しました");
        }
    }


    /**
     * argsのバリデーションチェック
     *
     * @author raindazo
     */
    @Override
    public boolean validation(String[] args) {
        final String ng = ".*[@＠｢「｣」：]+.*";

        if (args.length != 3) {
            System.out.println("入力されていない項目があります。");
            return true;
        }

        if (Stream.of(args).limit(2).anyMatch(path -> path.matches(ng))) {
            System.out.println(args[0] + "もしくは" + args[1] + "に使用できない文字(￥,：,＊,？,”,＜,＞,｜)が含まれています。\nパスを確認後、再度起動してください。");
            return true;
        }

        return false;
    }
}