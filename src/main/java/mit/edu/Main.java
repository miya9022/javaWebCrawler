package mit.edu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by app on 10/9/16.
 */
public class Main {
    public static DBHelper dbHelper = new DBHelper();

    public static void main (String [] args) throws SQLException, IOException {
        dbHelper.runSql2("TRUNCATE Record;");
        processPage(Constants.BASE_URL);
    }

    public static void processPage(String URL) throws SQLException, IOException {
        String sql = "select * from Record where URL = '" +URL+ "'";
        ResultSet rs = dbHelper.runSql1(sql);
        if(rs.next()) {

        } else {
            sql = "INSERT INTO Crawler.Record " + "(URL) VALUES " + "(?);";
            PreparedStatement stmt = dbHelper.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, URL);
            stmt.execute();

            Document doc = Jsoup.connect(Constants.BASE_URL).get();

            if(doc.text().contains("research")) {
                System.out.println(URL);
            }

            Elements questions = doc.select("a[href]");
            for(Element link: questions){
                if(link.attr("href").contains("mit.edu"))
                    processPage(link.attr("abs:href"));
            }
        }
    }
}
