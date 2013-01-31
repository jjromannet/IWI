/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.tweets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sem2.iwi.utils.IWIUtils;
import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 *
 * @author kubeusz
 */
public class GettingTweets {
    
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
                bw.write(i + " " + s + "\n");
                i++;
            }
            
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(GettingTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String gettingTweets(List<String> users, String noOfTweets){
        
        Twitter twitter = getTwitterInstance();
        
        FileWriter fw = null;
        BufferedWriter bw =null;
        String content = "";
        
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
                        
                        List<Status> statuses = twitter.getUserTimeline(result[1], new Paging(1,Integer.parseInt(noOfTweets) ));

                        for (Status status : statuses) {
                            bw.write(result[0] + " || " + status.getText() +"\n");
                            content+=result[0] + " || " + status.getText() +"\n";
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
    
    public static Twitter getTwitterInstance(){
        TwitterFactory factory = new TwitterFactory();
        
        Twitter twitter = factory.getInstance();

        AccessToken accessToken = new AccessToken(IWIUtils.getPropertyValue("oauth.accessToken"), IWIUtils.getPropertyValue("oauth.accessTokenSecret"));
        
        //twitter.setOAuthConsumer(IWIUtils.getPropertyValue("oauth.consumerKey"), IWIUtils.getPropertyValue("oauth.consumerSecret"));
        twitter.setOAuthAccessToken(accessToken);
    
        return twitter;
    }
     
}