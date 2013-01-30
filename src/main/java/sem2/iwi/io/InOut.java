/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import sem2.iwi.bayes.NBValuesVector;

/**
 *
 * @author jjroman
 */
public class InOut {

    public static HashMap<String, String> getInputStreamAsOutputDictionary(InputStream is) {
        HashMap<String, String> retVal = new HashMap<>();
        try (Scanner scan = new Scanner(is)) {
            int noOfOutputClasses = scan.nextInt();
            while (retVal.size() < noOfOutputClasses) {
                retVal.put(scan.next(), scan.next());
            }
        }
        return retVal;
    }

    public static ArrayList<NBValuesVector> getInputStreamAsVectorList(InputStream is) {
        ArrayList<NBValuesVector> retVal = new ArrayList<>();
        try (Scanner scan = new Scanner(is)) {
            int noOfLines = scan.nextInt();
            int noOfIntInLine = scan.nextInt();
            int noOfAttrInLine = noOfIntInLine - 1;
            String expectedValue = null;
            Integer[] buffor = new Integer[noOfAttrInLine];

            while (noOfLines-- > 0) {
                for (int i = 0; i < noOfAttrInLine; i++) {
                    buffor[i] = scan.nextInt();
                }
                expectedValue = scan.next();
                retVal.add(new NBValuesVector(expectedValue, buffor));
            }
        }
        return retVal;
    }
}
