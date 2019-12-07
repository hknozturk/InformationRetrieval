import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class HtmlParser {
    public static void main(String args[]) throws IOException {
        String filePath = "/home/hawken/InformationRetrieval/articles/a/a/a/AAA_Liberty_District_a4b5.html";
        Document htmlDoc = Jsoup.parse(new File(filePath), "utf-8");
        String title = htmlDoc.title();
        String body = htmlDoc.body().text();

        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
    }
}
