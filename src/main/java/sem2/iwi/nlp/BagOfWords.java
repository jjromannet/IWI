/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.nlp;

import java.util.ArrayList;
import java.util.Collections;
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
    private ArrayList<ArrayList<Integer>> categoryBands = new ArrayList<>();

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

    public void createNormalizationVector(ArrayList<ArrayList<Integer>> toNormalize) {
        categoryBands = new ArrayList<>();
        int sub = toNormalize.get(0).size() - 1;
// length + categories - wynik
        for (int categoryNo = 5; categoryNo < sub; categoryNo++) {
            ArrayList<Integer> forOneCategory = new ArrayList<>();
            for (int i = 0; i < toNormalize.size(); i++) {
                forOneCategory.add(toNormalize.get(i).get(categoryNo));
            }
            Collections.sort(forOneCategory);
            int sizeOfOne = forOneCategory.size() / 16;

            ArrayList<Integer> catBounds = new ArrayList<>();
            for (int j = sizeOfOne; j < forOneCategory.size(); j += sizeOfOne) {
                catBounds.add(forOneCategory.get(j));
            }
            categoryBands.add(catBounds);
        }
    }

    public ArrayList<ArrayList<Integer>> normalize(ArrayList<ArrayList<Integer>> toNormalize) {
        ArrayList<ArrayList<Integer>> retVal = new ArrayList<>();

        // dla każdego twita 
        for (ArrayList<Integer> arrayList : toNormalize) {
            // dla każdej kategori:
            ArrayList<Integer> newList = new ArrayList<>();
            for (int i2 = 0; i2 < 5; i2++) {
                newList.add(arrayList.get(i2));
            }
            for (int i2 = 0; i2 < categoryBands.size(); i2++) {
                int oryginalValue = arrayList.get(i2 + 5);
                int normalizedValue = 0;
                while (normalizedValue < categoryBands.get(i2).size()
                        && oryginalValue > categoryBands.get(i2).get(normalizedValue)) {
                    normalizedValue++;
                }
                System.out.println(i2 + " " + oryginalValue + " -> " + normalizedValue + " " + arrayList.get(i2));
                //arrayList.set(i2+5, normalizedValue);
                newList.add(normalizedValue);
            }
            newList.add(arrayList.get(arrayList.size() - 1));
            retVal.add(newList);
        }

        return retVal;
    }

    public ArrayList<Integer> scoreAgainsAll(String twit) {
        ArrayList<Integer> retVal = new ArrayList<>();
        twit = twit.replaceAll("http://[^ ]+", "");
        List<String> twitWords = Stemmer.getWordsOnlyForTags(twit, "NN", "VB");
        for (int i = 0; i < alhmsacBags.size(); i++) {
            retVal.add(scoreAgainstSpecific(twitWords, i));
        }
        return retVal;
    }

    public Integer scoreAgainstSpecific(String twit, Integer i) {
        List<String> twitWords = Stemmer.getWordsOnlyForTags(twit, "NN", "VB");
        return scoreAgainstSpecific(twitWords, i);
    }

    public Integer scoreAgainstSpecific(List<String> twitWords, Integer i) {
        HashMap<String, AtomicInteger> hm = alhmsacBags.get(i);
        Integer score = 0;

        for (String string : twitWords) {
            if (hm.containsKey(string)) {
                score += hm.get(string).get();
            }
        }
        return score;
    }
}
