/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jjroman
 */
public class BagOfWords {
    
    private ArrayList<HashMap<String, AtomicInteger>> alhmsacBags = new ArrayList<>();
    private ArrayList<AtomicInteger> alaiNoOfWords = new ArrayList<>();
    
    public void addNewBag(List<String> listOfTweets) {
        HashMap<String, AtomicInteger> hm = new HashMap<>();
        AtomicInteger ai = new AtomicInteger(0);
        for (String string : listOfTweets) {
            List<String> ls = Stemmer.getWordsOnlyForTags(string, "NN", "VB");
            for (String string1 : ls) {
                ai.incrementAndGet();
                if (hm.containsKey(string1)) {
                    hm.get(string1).incrementAndGet();
                } else {
                    hm.put(string1, new AtomicInteger(1));
                }
            }
        }
        alaiNoOfWords.add(ai);
        alhmsacBags.add(hm);
    }
    
    public ArrayList<Integer> scoreAgainsAll(String twit) {
        ArrayList<Integer> retVal = new ArrayList<>();
        for (int i = 0; i < alhmsacBags.size(); i++) {
            retVal.add(scoreAgainstSpecific(twit, i));
        }
        return retVal;
    }
    
    public Integer scoreAgainstSpecific(String twit, Integer i) {
        HashMap<String, AtomicInteger> hm = alhmsacBags.get(i);
        Integer score = 0;
        List<String> twitWords = Stemmer.getWordsOnlyForTags(twit, "NN", "VB");
        for (String string : twitWords) {
            if(hm.containsKey(string)){
                score += hm.get(string).get();
            }
        }
        return score;
    }
}
