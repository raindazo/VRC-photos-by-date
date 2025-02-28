package Util;

import exception.MyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class ImplementTaskSchedulerUtils implements CreateTaskScheduler {

    @Override
    public void readXml(String xmlPath) {
        try {
            //xmlを1行ずつ取得(1行ごとにブランク行ができてしまうためfilterで取り除く)
            createXml(Files.readAllLines(Path.of(xmlPath)).stream().filter(v -> !v.isBlank()).toList(), xmlPath);
        } catch (IOException e) {
            new MyException(e);
        }
    }

    @Override
    public void createXml(List<String> xml, String xmlPath) {
        //<Arguments>が含まれていれば一度取り除く
        List<String> processedLsit = xml.stream().filter(v -> !v.contains("<Arguments>")).toList();

        Path path = Path.of(xmlPath).getParent();

        //<Command>行を取得
        String commandAttribute = processedLsit.stream()
                .filter(v -> v.contains("<Command>"))
                .findFirst()
                .orElse("");

        //CreatePictureFile.batが設定されているパスじゃなければ修正する
        if (!commandAttribute.contains("CreatePictureFile.bat")) {
            commandAttribute = new StringBuilder(commandAttribute)
                    .insert(commandAttribute.length() - 10, "\\CreatePictureFile.bat")
                    .toString();
        }

        String finalCommandAttribute = commandAttribute;
        String workingDirectoryAttribute = "<WorkingDirectory>" + path + "\\</WorkingDirectory>";
        String argumentsAttribute = "<Arguments>scheduler</Arguments>";
        List<String> outputList = processedLsit.stream()
                .flatMap(v -> v.contains("<Command>")
                        ? Stream.of(finalCommandAttribute, argumentsAttribute, workingDirectoryAttribute)
                        : Stream.of(v))
                .toList();

        try {
            Path output = Paths.get(path.toString(), "modified.xml");
            Files.write(output, outputList, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            new MyException(e);
        }
    }
}
