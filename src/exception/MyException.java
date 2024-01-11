package exception;

public class MyException {
    public MyException(Exception e) {
        System.out.println("例外が発生しました。");
        System.out.println("解決できない場合は、お手数おかけしますが、以下のログと合わせて開発者にご連絡ください。");
        e.printStackTrace();
        System.exit(0);
    }
}