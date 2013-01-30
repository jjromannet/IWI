/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author jjroman
 */
public class MainTwellowCrawler {

    private static final String TWELLOW_BASE = "http://www.twellow.com/";
    static HttpClient client = new DefaultHttpClient();
    static HttpContext hc = new BasicHttpContext();
    static CookieStore cs = new BasicCookieStore();

    static {
        hc.setAttribute(ClientContext.COOKIE_STORE, cs);
    }

    public static void main(String[] args) {
        /*
         System.out.print(getUrlAsString2("http://jjroman.net/ip.php"));
         System.out.print("\n\n-----------\n\n");
         System.out.print(getUrlAsString2("http://jjroman.net/ip.php"));
         System.out.print("\n\n-----------\n\n");
         */


        HashSet<String> setOfChoosen = new HashSet<>();
        Collections.addAll(setOfChoosen, "Education", "Games",
                "Movies & Filmmaking", "Sports", "Pets", "Travel", "Politics",
                "Fashion", "Actors & Actresses", "Fitness");


        HashSet<TwellowCategory> atc = getMainCategoryList();
        StringBuilder output = new StringBuilder();

        for (TwellowCategory tc : atc) {
            if (!setOfChoosen.contains(tc.getName())) {
                continue;
            }
            System.out.println(String.format("%s\t%d\t http://twellow.com%s ", tc.getName(), tc.getUsers(), tc.getUrl()));

            ArrayList<String> nicks = getNicksForCategory(tc);
            for (String nick : nicks) {
                output.append(tc.getName())
                        .append('\t')
                        .append(nick)
                        .append('\n');
            }
            System.out.print(output);
            try {
                FileUtils.write(new File("nicks_outpur.tab"), output);
            } catch (IOException ex) {
                Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

     
        /*for (String nick : nicks) {
         System.out.println(nick);
         }*/

    }

    public static String getNicksForCategoryNames(ArrayList<String> categories) {
        
        HashSet<TwellowCategory> atc = getMainCategoryList();
        StringBuilder output = new StringBuilder();

        for (TwellowCategory tc : atc) {
            if (!categories.contains(tc.getName())) {
                continue;
            }
            //System.out.println(String.format("%s\t%d\t http://twellow.com%s ", tc.getName(), tc.getUsers(), tc.getUrl()));

            ArrayList<String> nicks = getNicksForCategory(tc);
            for (String nick : nicks) {
                output.append(tc.getName())
                        .append('\t')
                        .append(nick)
                        .append('\n');
            }
        }
        return output.toString();
    }

    private static String getUrlForCategory(TwellowCategory tc) {
        return TWELLOW_BASE + tc.getUrl();
    }

    public HashMap<TwellowCategory, ArrayList<String>> getPeopleForCategories(ArrayList<TwellowCategory> altc) {
        HashMap<TwellowCategory, ArrayList<String>> retVal = new HashMap<>();

        return retVal;
    }

    public static ArrayList<String> getNicksForCategory(TwellowCategory tc) {
        ArrayList<String> retVal = new ArrayList<>();
        String input = getUrlAsString2(getUrlForCategory(tc));

        String pat = "<a href=\"/([^/\"]+)\">([^<]+)</a> <span>\\(([^\\)]*)\\)</span>";
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(input);

        while (m.find()) {
            retVal.add(m.group(3));
        }
        return retVal;
    }

    public static HashSet<TwellowCategory> getMainCategoryList() {
        HashSet<TwellowCategory> retVal = new HashSet<>();

        String homepage = getUrlAsString("homepage.html");

        String pat = "<a href=\"/categories/([^\"]+)\">([^<]+)</a>[^\\(<]*\\(([0-9,]+)\\)";
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(homepage);

        while (m.find()) {
            retVal.add(new TwellowCategory(m.group(2), "/categories/" + m.group(1), Integer.parseInt(m.group(3).replace(",", ""))));
        }

        return retVal;
    }

    private static String getUrlAsString2(String url) {
        return getUrlAsString2(url, false);
    }

    private static String getUrlAsString2(String url, boolean forced) {
        HttpGet request = new HttpGet(url);


        String tmpurl = url.replace("categories/", "categories_");
        String[] uarr = tmpurl.split("/");
        tmpurl = uarr[uarr.length - 1];
        tmpurl += ".html";
        File f = new File(tmpurl);

        if (!forced) {
            if (f.exists()) {
                String forReturn = "";
                try {
                    forReturn = FileUtils.readFileToString(f);
                    return forReturn;
                } catch (IOException ex) {
                    Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        request.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        //request.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        request.setHeader("Accept-Language", "en-US,en;q=0.8");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Host", "www.twellow.com");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
        request.setHeader("Referer", "http://www.twellow.com/");

        String retVal = "";
        try {
            HttpResponse response = client.execute(request, hc);

            retVal = IOUtils.toString(response.getEntity().getContent());
        } catch (IOException ex) {
            Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileUtils.write(f, retVal);
        } catch (IOException ex) {
            // swallow - it is just cach what is not working
            Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retVal;
    }

    private static String getUrlAsString(String url) {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
            StringWriter sw = new StringWriter();
            IOUtils.copy(is, sw, "UTF-8");
            return sw.toString();
        } catch (IOException ex) {
            Logger.getLogger(MainTwellowCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
