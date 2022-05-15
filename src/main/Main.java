package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    public static void main(String[] args) throws IOException{
        // 変数定義
        int systemYear = YearMonth.now().getYear(); // 今年
        Document allYearHtml = (Document) Jsoup.connect("http://www.baseball-reference.com/register/league.cgi?code=JPPL&class=Fgn").get(); // スクレイピング対象のページ
        Elements linkAllyaerTable = allYearHtml.select("#lg_history tbody");  // 毎年度、全チームの情報が入ったテーブル

        Date dateObj = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeforLog = format.format( dateObj );

        try{

            File file = new File("c:\\pleiades\\workspace\\ScrapingTest\\log\\" + timeforLog + ".log");
            FileWriter filewriter = new FileWriter(file);
            for (int yearID = 0; yearID < (systemYear - 1950 + 1); yearID++){

                // 変数定義
                int targetYear = (systemYear - yearID); // データ取得対象年度
                Elements targetYearElements = linkAllyaerTable.get(0).select("th"); // YearID年度とリンクを入れるリスト
                Elements teamElements = linkAllyaerTable.get(0).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

                // 年度ごとに段落を刻む
                filewriter.write( "シーズン：" +  targetYearElements.select("a").get(yearID).ownText() + ":" + targetYearElements.select("a").get(yearID).absUrl("href") + "\r\n");

                // リンクとチーム名を持ったaタグを取得
                for (Element targetTeam: teamElements){
                    String teamName = targetTeam.ownText();
                    String teamLink = targetTeam.absUrl("href");
                    filewriter.write( targetYear +  ":" + teamName +  ":" + teamLink  + "\r\n");
                }

                filewriter.write("\r\n");
            }

            filewriter.close();

        }catch(IOException e){
            System.out.println(e);
          }




    }
}
