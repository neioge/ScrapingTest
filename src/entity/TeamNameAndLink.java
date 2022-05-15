package entity;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.LinkedHashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TeamNameAndLink {

    // フィールド
    String targetYear;
    String targetYearLink;
    LinkedHashMap<Integer, String> teamList = new LinkedHashMap<>();

    // デフォルトコンストラクタ
    public TeamNameAndLink(){
        super();
    }

    // 実際に使うコンストラクタ
    public TeamNameAndLink(Document allYearHtml){

        int systemYear = YearMonth.now().getYear(); // 今年
        Elements linkAllyaerTable = allYearHtml.select("#lg_history tbody");  // 毎年度、全チームの情報が入ったテーブル

        Date dateObj = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeforLog = format.format( dateObj );

        for (int yearID = 0; yearID < (systemYear - 1950 + 1); yearID++){

            // 変数定義
            int targetYear = (systemYear - yearID); // データ取得対象年度
            Elements targetYearElements = linkAllyaerTable.get(0).select("th"); // YearID年度とリンクを入れるリスト
            Elements teamElements = linkAllyaerTable.get(0).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

            // 年度ごとに段落を刻む

            // リンクとチーム名を持ったaタグを取得
            for (Element targetTeam: teamElements){
                String teamName = targetTeam.ownText();
                String teamLink = targetTeam.absUrl("href");
            }
        }
    }
}
