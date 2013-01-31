/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jjroman
 */
public class NaiveClassifier {

    private NBAttribute[] attributes = null;
    private HashMap<String, NBOutputValue> ovMap = null;

    /**
     *
     * @param noOfAttributes
     * @param ovMap
     */
    public NaiveClassifier(int attributeVectorLength, Map<String, String> ovMap) {
        initAttributes(attributeVectorLength);
        initOutputValues(ovMap);
    }

    private void initAttributes(int attributeVectorLength) {
        this.attributes = new NBAttribute[attributeVectorLength];
        for (int i = 0; i < attributeVectorLength; i++) {
            this.attributes[i] = new NBAttribute();
        }
    }

    private void initOutputValues(Map<String, String> ovMap) {
        this.ovMap = new HashMap<>();

        for (Map.Entry<String, String> me : ovMap.entrySet()) {
            this.ovMap.put(me.getKey(), new NBOutputValue(me.getKey(), me.getValue()));
        }
    }

    public NBAttribute getAttributeForIndex(int index) {
        return this.attributes[index];
    }

    public NBOutputValue getOutputValueForString(String ov) {
        return this.ovMap.get(ov);
    }

    public void learnNextVector(NBValuesVector nbv, NBOutputValue nbov) {
        for (int i = 0; i < attributes.length; i++) {
            attributes[i].incrementValue(nbv.get(i), nbov);
        }
    }

    public void train(NBAttribute attr, NBAttributeValue attrVal, NBOutputValue outVal) {
        attr.incrementValue(attrVal, outVal);
    }

    public NBOutputValue classifyVector(NBValuesVector nbValuesVector) {

        double max = 0.0, tmp;
        NBOutputValue retVal = null;
        for (NBOutputValue nbov : ovMap.values()) {
            tmp = classifyVectorForValue(nbValuesVector, nbov);
            if (tmp > max) {
                max = tmp;
                retVal = nbov;
            }
            System.out.print(String.format("%s, %f\n", nbov.name, tmp));
        }
        return retVal;
        /**
         * Bierzemy Output 1. Mnozymy wektor
         *
         * Bierzemy Output 2. Mnozymy wektor ....
         *
         */
    }

    private double classifyVectorForValue(NBValuesVector nbValuesVector, NBOutputValue nbov) {
        double summarised = 1.0;
        for (int i = 0; i < attributes.length; i++) {
            summarised *= attributes[i].getProbablity(nbValuesVector.get(i), nbov);
        }
        return summarised;
    }

    public NBScores test(ArrayList<NBValuesVector> alnbvv) {
        NBScores nbs = new NBScores();
        
        for (NBValuesVector nBValuesVector : alnbvv) {
            NBOutputValue nbov = classifyVector(nBValuesVector);
            if(nbov.equals(getOutputValueForString(nBValuesVector.getExpectedValue()))){
                nbs.addPositive(nbov);
            }else{
                nbs.addNegative(getOutputValueForString(nBValuesVector.getExpectedValue()));
            }
        }
        return nbs;
    }
}
