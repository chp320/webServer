package testBoard;

import java.util.ArrayList;
import java.util.Date;

public class Article {
    public static ArrayList<Article> articleList = new ArrayList<Article>();
    String title;
    String handle;
    String message;
    Date date;

    public Article(String title, String handle, String message) {
        this.title = title;
        this.handle = handle;
        this.message = message;
        this.date = new Date();
    }
}
