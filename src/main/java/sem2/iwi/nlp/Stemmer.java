/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.nlp;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjroman
 */
public class Stemmer {

    static String forPosTagging = "PM David Cameron will visit #Algeria tomorrow for bilateral meetings";

    public static void main(String[] args) {
        
        List<String> wl = getWordsOnlyForTags(forPosTagging, "NN", "VB");
        for(String s : wl){
            System.out.println(s);
        }

    }

    private static ArrayList<String> getWordsOnlyForTags(String forPosTagging, String... tags) {
        ArrayList<String> retVal = new ArrayList<>();
        
        try {
            MaxentTagger tagger = new MaxentTagger("english-left3words-distsim.tagger");
            List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(forPosTagging));

            for (List<HasWord> sentence : sentences) {

                ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
                for (TaggedWord tw : tSentence) {
                    for (String tag : tags) {
                        if (tw.tag().startsWith(tag)) {
                            WordTag wt = Morphology.stemStatic(tw.value(), tw.tag());
                            //System.out.print(String.format("oryg: %s[%s]\tstemm: %s[%s]\n", tw.value(), tw.tag(), wt.word(), wt.tag()));
                            retVal.add(wt.word());
                            break;
                        }
                    }
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(Stemmer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Stemmer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retVal;

    }
}
