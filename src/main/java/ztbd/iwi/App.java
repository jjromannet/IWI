package ztbd.iwi;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            NaiveClassifier nc;
            try (Scanner scan = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("input2_test.tab"))) {
                int noOfOutputClasses = scan.nextInt();
                HashMap<String, String> hm = new HashMap<>();
                while (hm.size() < noOfOutputClasses) {
                    hm.put(scan.next(), scan.next());
                }
                int noOfLines = scan.nextInt();
                int noOfIntInLine = scan.nextInt();
                int noOfAttrInLine = noOfIntInLine - 1;
                String expectedValue = null;
                Integer[] buffor = new Integer[noOfAttrInLine];
                nc = new NaiveClassifier(noOfAttrInLine, hm);
                while (noOfLines-- > 0) {
                    for (int i = 0; i < noOfAttrInLine; i++) {
                        buffor[i] = scan.nextInt();
                    }
                    expectedValue = scan.next();

                    for (int i = 0; i < noOfAttrInLine; i++) {
                        nc.getAttributeForIndex(i).incrementValue(new NBAttributeValue(buffor[i]), nc.getOutputValueForString(expectedValue));
                    }
                }
            }

            //nc.getVectorProbabilityForAnswer(NBVector nbVec, NBOutputValue ov);
            //nc.klasifyVector();
            System.out.println("Probability of getting answer 1 for R-1 classification with attribute number 1 of value 0");
            System.out.println(nc.getAttributeForIndex(1).getProbablity(new NBAttributeValue(0), nc.getOutputValueForString("1")));

            System.out.println("Probability of getting answer 1 for R-1 classification with attribute number 2 of value 3");
            System.out.println(nc.getAttributeForIndex(2).getProbablity(new NBAttributeValue(3), nc.getOutputValueForString("1")));

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
