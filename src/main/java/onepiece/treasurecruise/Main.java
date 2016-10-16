package onepiece.treasurecruise;

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

    private static DBHelper dbHelper = new DBHelper();

    public static void main(String [] args) throws SQLException, IOException {
        dbHelper.runSql2("TRUNCATE LinkInfo;");
        processPage(Constants.URL_CHARACTER);
    }

    private static void processPage(String URL) throws SQLException, IOException {
        String sql = "select * from LinkInfo where URL_DIRECT = '" +URL+ "'";
        ResultSet rs = dbHelper.runSql1(sql);

        if(rs.next()) {

        } else {
            Document doc = Jsoup.connect(Constants.URL_CHARACTER).get();

            Elements table = doc.select(".pc");
//            System.out.println(table.toString());
            for (Element link: table) {
                String title = link.select("a[href]").attr("title");
                String urlDirect = link.select("a[href]").attr("href");
                String urlImg = link.select("img").attr("src");

                insertLink(title, urlDirect, urlImg);
                System.out.println(urlDirect);
                proceedContent(urlDirect);
            }

        }
    }

    private static void insertLink(String title, String urlDirect, String urlImg) throws SQLException {
        String sql = "INSERT INTO LinkInfo " + "(title, URL_DIRECT, URL_IMG) VALUES " + "(?,?,?);";
        PreparedStatement stmt = dbHelper.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, title);
        stmt.setString(2, urlDirect);
        stmt.setString(3, urlImg);
        stmt.execute();
    }

    private static void proceedContent(String url) throws SQLException, IOException {
        Document doc = Jsoup.connect(url).get();

    }
}
