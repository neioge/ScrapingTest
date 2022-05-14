package main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) {

        try {
            // jsoupを使用して当ブログのトップページへアクセス
            Document doc = Jsoup.connect("https://www.baseball-reference.com/register/league.cgi?code=JPCL&class=Fgn").get();

            Elements elements = doc.select("h3");

            for (Element element : elements) {
                System.out.println(element.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
