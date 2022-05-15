package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            File logFile = new File("c:\\pleiades\\workspace\\ScrapingTest\\log\\" + timeforLog + ".log");
            FileWriter logFilewriter = new FileWriter(logFile);

            for (int yearID = 0; yearID < (systemYear - 1950 + 1); yearID++){

                // 変数定義
                int targetYear = (systemYear - yearID); // データ取得対象年度
                Elements targetYearElements = linkAllyaerTable.get(0).select("th"); // YearID年度とリンクを入れるリスト
                Elements teamElements = linkAllyaerTable.get(0).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

                Path yearPath = Paths.get("c:\\pleiades\\workspace\\ScrapingTest\\output\\" + targetYear);

                try{
                  if (! (Files.exists(yearPath))){
                      Files.createDirectory(yearPath);
                  }
                }catch(IOException e){
                  System.out.println(e);
                }

                // 年度ごとに段落を刻む
                logFilewriter.write( "シーズン：" +  targetYearElements.select("a").get(yearID).ownText() + ":" + targetYearElements.select("a").get(yearID).absUrl("href") + "\r\n");

                // リンクとチーム名を持ったaタグを取得
                for (Element targetTeam: teamElements){
                    String teamName = targetTeam.ownText().replace(" ", "");
                    String teamLink = targetTeam.absUrl("href");
                    logFilewriter.write( targetYear +  ":" + teamName +  ":" + teamLink  + "\r\n");

                    Document teamHtml = (Document) Jsoup.connect(teamLink).get(); // year年の選手一覧のページ
                    Elements playersStutsTable = teamHtml.select("#team_batting tbody tr");  // 各チーム、全選手の情報が入ったテーブル

                    try {
                        File outputFile = new File("c:\\pleiades\\workspace\\ScrapingTest\\output\\" + targetYear + "\\"  + teamName + ".csv");
                        FileWriter outputFileWriter = new FileWriter(outputFile);
                        outputFileWriter.write("Name" + "," + "Average" + "," + "Homerun" + "," + "RBI" + "," + "OPS" + "," + "StolenBase" + "\r\n");
                        for(Element targetPlayerElements : playersStutsTable){
                            Elements targetPlayer_tds = targetPlayerElements.select("td"); // 選手のtdを格納するリスト
                            String targetPlayerName = targetPlayer_tds.get(0).select("a").get(0).ownText();
                            String targetPlayerAvg = targetPlayer_tds.get(15).ownText();
                            String targetPlayerHR = targetPlayer_tds.get(9).ownText();
                            String targetPlayerRBI = targetPlayer_tds.get(10).ownText();
                            String targetPlayerOPS = targetPlayer_tds.get(18).ownText();
                            String targetPlayerSB = targetPlayer_tds.get(11).ownText();
                            outputFileWriter.write(targetPlayerName + "," + targetPlayerAvg + "," + targetPlayerHR + "," + targetPlayerRBI + "," + targetPlayerOPS + "," + targetPlayerSB + "\r\n");
                        }
                        outputFileWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                logFilewriter.write("\r\n");
            }

            logFilewriter.close();

        }catch(IOException e){
            System.out.println(e);
          }




    }
}
