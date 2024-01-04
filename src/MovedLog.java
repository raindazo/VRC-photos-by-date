import java.util.ArrayList;
import java.util.List;

public class MovedLog {

    private static List<String> movedlList = new ArrayList<>();

    // リストに要素を追加するメソッド
    public static void addList(String picture) { movedlList.add(picture); }

    // リストから要素を取得するメソッド
    public static ArrayList<String> getList() { return new ArrayList<String>(movedlList); }

}
