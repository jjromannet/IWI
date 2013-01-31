/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.tweets;

import com.sun.imageio.plugins.common.BogusColorSpace;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sem2.iwi.bayes.NBOutputValue;
import sem2.iwi.nlp.BagOfWords;
import sem2.iwi.utils.IWIUtils;
import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 *
 * @author kubeusz
 */
public class GettingTweets {
    private final static BagOfWords bow = new BagOfWords();

    public static void writeToFileTwellowCategories(String content){
        
        FileWriter fw = null;
        try {
            fw = new FileWriter(IWIUtils.getPropertyValue("categoriesUserFile"));
            
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(content);
            
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static List<String> readFromFileTwellowCategories(){
        
        HashSet<String> categories=new HashSet<String>();
        List<String> users = new ArrayList<String>();
        
        Path path = Paths.get(IWIUtils.getPropertyValue("categoriesUserFile"));
        try {
            Scanner scanner =  new Scanner(path);
            
            while (scanner.hasNextLine()){

                String[] result = scanner.nextLine().split("\\t");
                
                categories.add(result[0]);
                users.add(result[0].concat(result[1]));
                
            }
            scanner.close();
        } catch (IOException ex) {
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        createCategoriesDictionary(categories);
        
        return users;
        
    }
    
    public static void createCategoriesDictionary(HashSet<String> categories){
        
        FileWriter fw = null;
        try {
            fw = new FileWriter(IWIUtils.getPropertyValue("categoriesDictionaryFile"));
            
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(Integer.toString(categories.size()));
            bw.write("\n");
            
            int i=1;
            
            for(String s : categories){
                bw.write(i + "\t" + s + "\n");
                i++;
            }
            
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    static Map<String,Integer> categoryMappings = null;
    
    public static Map<String,Integer> readCategoriesDictionary(){
        if(categoryMappings == null){
            categoryMappings = new HashMap<String,Integer>();

            Path path = Paths.get(IWIUtils.getPropertyValue("categoriesDictionaryFile"));
            try {
                Scanner scanner =  new Scanner(path);

                while (scanner.hasNextLine()){

                    String[] result = scanner.nextLine().split("\t");

                    if(result.length!=1){
                        categoryMappings.put( result[1],Integer.parseInt(result[0]));
                    }

                }
                scanner.close();
            } catch (IOException ex) {
                Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return categoryMappings;
    }
    private static HashMap<String, ArrayList<String>> forBOW = null;
    public static String gettingTweets(List<String> users, String noOfTweets, String noOfPage){
        
        Twitter twitter = getTwitterInstance();
        
        FileWriter fw = null;
        BufferedWriter bw =null;
        String content = "";
        forBOW = new HashMap<>();
        
        try {
            fw = new FileWriter(IWIUtils.getPropertyValue("usersTweetsFile"));
            
            bw = new BufferedWriter(fw);
            
                for(String u : users){
                    
                    String[] result = u.split("@");
                    
                    boolean exists = false;
                    try{
                        twitter.showUser(result[1]);
                        exists = true;
                    }catch(TwitterException te){
                        if(te.getStatusCode() == 404){
                            exists = false;
                        } else{
                                
                              Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null,"Account " + result[1] + "doesn't exist anymore.\n" + te);
                        }
                    }
                    
                    if(exists!=false){
                        
                        List<Status> statuses = twitter.getUserTimeline(result[1], new Paging(Integer.parseInt(noOfPage),Integer.parseInt(noOfTweets) ));

                        for (Status status : statuses) {
                            String s = status.getText();
                            s.replace("\n\r", " ");
                            s = removeSpecialCharacters(s);
                            if(forBOW.containsKey(result[0])){
                                forBOW.get(result[0]).add(s);
                            }else{
                                ArrayList<String> altmp = new ArrayList<>();
                                altmp.add(s);
                                forBOW.put(result[0], altmp);
                            }
                            bw.write(result[0] + " || " + s +"\n");
                            content+=result[0] + " || " + s +"\n";
                        }
                        
                    }

                }
            
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TwitterException tex){
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, tex);
        }
        
        
        
        return content;
    }
    
    public static ArrayList<ArrayList<Integer>> getForBayes(boolean newBOW ){
        
        if(newBOW){
            for (Map.Entry<String, ArrayList<String>> me : forBOW.entrySet()) {
                bow.addNewBag(me.getValue());
            }
        }
        
        ArrayList<ArrayList<Integer>> retVal = new ArrayList<>();
        if(forBOW != null){
            for (Map.Entry<String, ArrayList<String>> me : forBOW.entrySet()) {
                    for (String twit : me.getValue()) {
                        retVal.add(getAllMetrics(twit, me.getKey()));
                    }
                }
        }
        if(newBOW){
            bow.createNormalizationVector(retVal);
        }
        retVal = bow.normalize(retVal);
        return retVal;
    }
    
    public static Twitter getTwitterInstance(){
        TwitterFactory factory = new TwitterFactory();
        
        Twitter twitter = factory.getInstance();

        AccessToken accessToken = new AccessToken(IWIUtils.getPropertyValue("oauth.accessToken"), IWIUtils.getPropertyValue("oauth.accessTokenSecret"));
        
        //twitter.setOAuthConsumer(IWIUtils.getPropertyValue("oauth.consumerKey"), IWIUtils.getPropertyValue("oauth.consumerSecret"));
        twitter.setOAuthAccessToken(accessToken);
    
        return twitter;
    }
     
    public static Integer[] getGenericMetrics(String twit){
    
        Integer[] metrics = new Integer[6];
        
        Pattern patternHash = Pattern.compile("#"), patternMention = Pattern.compile("@"), patternUrl = Pattern.compile("http://");
        
        Matcher  matcherHash = patternHash.matcher(twit), matcherMention = patternMention.matcher(twit), matcherUrl = patternUrl.matcher(twit);

        int count = 0;
        while (matcherHash.find())
            count++;

        metrics[0] = count;
        
        count=0;
        while (matcherMention.find())
            count++;

        metrics[1] = count;
        
        count=0;
        while (matcherUrl.find())
            count++;

        metrics[2] = count;
        
        List<String> wl = sem2.iwi.nlp.Stemmer.getWordsOnlyForTags(twit,  "VB");
        
        metrics[3] = wl.size();
        
        wl.removeAll(wl);
        
        wl = sem2.iwi.nlp.Stemmer.getWordsOnlyForTags(twit,  "NN");
        
        metrics[4] = wl.size();
        
        metrics[5] = twit.length();
        
        //System.out.println(metrics[3]);
        //System.out.println(metrics[4]);
                
        return metrics;
    }
     public static ArrayList<Integer> getAllMetrics(String twit, String category){
        ArrayList<Integer> retVal = new ArrayList<>();
        Collections.addAll(retVal, getGenericMetrics(twit));
        retVal.addAll(bow.scoreAgainsAll(twit));
        if(category != null){
            retVal.add(readCategoriesDictionary().get(category));
        }
        return retVal;
    }
     
     public static String removeSpecialCharacters(String input){
        
        String output; 
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",
                Pattern.UNICODE_CASE | Pattern.CANON_EQ
                        | Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input);

        output = unicodeOutlierMatcher.replaceAll("");
        
        return output;
         
     }
           
}
