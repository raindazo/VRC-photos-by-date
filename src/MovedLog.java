import java.util.ArrayList;
import java.util.List;

public class MovedLog {

    private static final List<String> movedlList = new ArrayList<>();

    // リストに要素を追加
    public static void addList(String picture) { movedlList.add(picture); }

    // リストから要素を取得
    public static ArrayList<String> getList() { return new ArrayList<>(movedlList); }

}
