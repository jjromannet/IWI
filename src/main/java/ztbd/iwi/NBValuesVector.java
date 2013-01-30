/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztbd.iwi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author jjroman
 */
class NBValuesVector implements Iterable<NBAttributeValue> {

    private ArrayList<NBAttributeValue> alAtrrs = new ArrayList<>();
    private String expectedValue = null;
    public NBValuesVector(String expectedValue, Integer... vals) {
        this.expectedValue = expectedValue;
        for(Integer i : vals){
            alAtrrs.add(new NBAttributeValue(i));
        }
    }
    public NBValuesVector(NBAttributeValue... vals) {
        Collections.addAll(alAtrrs, vals);

    }

    @Override
    public Iterator<NBAttributeValue> iterator() {
        return alAtrrs.iterator();
    }

    NBAttributeValue get(int i) {
        return alAtrrs.get(i);
    }

    String getExpectedValue() {
        return this.expectedValue;
    }

    int size() {
        return alAtrrs.size();
    }
}
