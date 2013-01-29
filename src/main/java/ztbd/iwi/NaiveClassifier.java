/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztbd.iwi;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jjroman
 */
public class NaiveClassifier {

    private NBAttribute[] attributes = null;
    private HashMap<String,NBOutputValue> ovMap = null;

    /**
     * 
     * @param noOfAttributes
     * @param ovMap 
     */
    public NaiveClassifier(int noOfAttributes, Map<String,String> ovMap) {
       initAttributes(noOfAttributes); 
       initOutputValues(ovMap);
    }

    
    
    private void initAttributes(int noOfAttributes) {
        this.attributes = new NBAttribute[noOfAttributes];
        for (int i = 0; i < noOfAttributes; i++) {
            this.attributes[i] = new NBAttribute();
        }
    }

    private void initOutputValues(Map<String,String> ovMap) {
        this.ovMap = new HashMap<>();
        
        for(Map.Entry<String, String> me : ovMap.entrySet()){
            this.ovMap.put(me.getKey(), new NBOutputValue(me.getKey(), me.getValue()));
        }
    }

    public NBAttribute getAttributeForIndex(int index) {
        return this.attributes[index];
    }

    public NBOutputValue getOutputValueForString(String ov) {
        return this.ovMap.get(ov);
    }

    public void train(NBAttribute attr, NBAttributeValue attrVal, NBOutputValue outVal) {
        attr.incrementValue(attrVal, outVal);
    }
    
}
