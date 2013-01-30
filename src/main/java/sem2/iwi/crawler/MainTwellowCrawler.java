/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jjroman
 */
public class MainTwellowCrawler {

    public static void main(String[] args) {
        ArrayList<TwellowCategory> atc = (new MainTwellowCrawler()).getCategoryList();
        for(TwellowCategory tc : atc){
            System.out.println(String.format("%s[%d] http://twellow.com%s ", tc.getName(), tc.getUsers(), tc.getUrl()));
        }
    }

    public ArrayList<TwellowCategory> getCategoryList() {
        ArrayList<TwellowCategory> retVal = new ArrayList<>();

        String homepage = "<li><a href=\"/categories/augmented_reality\">Augmented Reality</a> (394)</li>";
        homepage = getUrlAsString("homepage.html");
        //homepage = "<li><a href=\"/categories/sports\">Sports</a> (98,406)</li>";
        String pat = "/<a href=\"/categories/([a-z_]+)\">([^<]+)</a>.*\\(([0-9,]+)\\)/";
        pat = "<a href=\"/categories/([^\"]+)\">([^<]+)</a>[^\\(<]*\\(([0-9,]+)\\)";
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(homepage);

        while (m.find()) {
            retVal.add( new TwellowCategory(m.group(2), "/categories/"+m.group(1), Integer.parseInt(m.group(3).replace(",", ""))));
        }

        return retVal;
    }

    private String getUrlAsString(String url) {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("homepage.html");
            StringWriter sw = new StringWriter();
            IOUtils.copy(is, sw, "UTF-8");
            return sw.toString();
        } catch (IOException ex) {
            Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
