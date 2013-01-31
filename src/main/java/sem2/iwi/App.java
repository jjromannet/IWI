package sem2.iwi;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import sem2.iwi.bayes.NBAttributeValue;
import sem2.iwi.bayes.NBOutputValue;
import sem2.iwi.bayes.NBValuesVector;
import sem2.iwi.bayes.NaiveClassifier;
import sem2.iwi.gui.MainWindow;
import sem2.iwi.io.InOut;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        JFrame f = new MainWindow();
        f.setVisible(true);
    }
    public static void testRun(){
        try {

            HashMap<String, String> hm = InOut.getInputStreamAsOutputDictionary(Thread.currentThread().getContextClassLoader().getResourceAsStream("input2_test.dict"));
            ArrayList<NBValuesVector> alnbvv = InOut.getInputStreamAsVectorList(Thread.currentThread().getContextClassLoader().getResourceAsStream("input2_test.tab"));

            NaiveClassifier nc = new NaiveClassifier(alnbvv.get(0).size(), hm);

            for (NBValuesVector nbvv : alnbvv) {
                nc.learnNextVector(nbvv, nc.getOutputValueForString(nbvv.getExpectedValue()));
            }

            // klasyfikacja wektora 1,1,3,0 wartosc ExpectedValue podana tylko 
            // w celach referencyjnych
            NBOutputValue nbov = nc.classifyVector(new NBValuesVector("1", 1, 1, 3, 0));
            System.out.println(nbov.getName());


            System.out.println("Probability of getting answer 1 for R-1 classification with attribute number 1 of value 0");
            System.out.println(nc.getAttributeForIndex(1).getProbablity(new NBAttributeValue(0), nc.getOutputValueForString("1")));

            System.out.println("Probability of getting answer 1 for R-1 classification with attribute number 2 of value 3");
            System.out.println(nc.getAttributeForIndex(2).getProbablity(new NBAttributeValue(3), nc.getOutputValueForString("1")));

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
