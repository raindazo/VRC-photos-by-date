package field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovedLog {

    private static final Map<String,String> movedlList = new HashMap<>();

    // マップに要素を追加
    public static void addEntry(String picture,String destinationPath) { movedlList.put(picture,destinationPath); }

    // リストから要素を取得
    public static List<String> getList() { return new HashMap<>(movedlList).entrySet().stream()
            .map(entry -> entry.getKey() + " -> " + entry.getValue()).toList(); }
}
