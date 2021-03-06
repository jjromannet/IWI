/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.bayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jjroman
 */
public class NBAttribute {
    private int summarisedNoOfEntities = 0;
    HashMap <NBAttributeValue, HashMap<NBOutputValue, AtomicInteger>> probabilityMap = new HashMap<>();
    
    /**
     * Metoda używana do inkrementalnego uczenia 
     * @param attrVal
     * @param outVal 
     */    
    public void incrementValue(NBAttributeValue attrVal, NBOutputValue outVal) {
        // sprawdzenie istnienia
        if(probabilityMap.containsKey(attrVal)){
           if(probabilityMap.get(attrVal).containsKey(outVal)){
               probabilityMap.get(attrVal).get(outVal).incrementAndGet();
           }else{
               probabilityMap.get(attrVal).put(outVal, new AtomicInteger(1));
           }
        }else{
            HashMap<NBOutputValue, AtomicInteger> hm = new HashMap<>();
            hm.put(outVal, new AtomicInteger(1));
            probabilityMap.put(attrVal,hm);
        }
        summarisedNoOfEntities++;
    }
    
    public double getProbablity(NBAttributeValue attrVal, NBOutputValue outVal){
        if(summarisedNoOfEntities > 0 
                && probabilityMap.containsKey(attrVal)
                && probabilityMap.get(attrVal).containsKey(outVal)
            ){
            int sum = 0;
            for ( AtomicInteger object : probabilityMap.get(attrVal).values()) {
                sum += object.get();
            }
                    
            return ((double)(probabilityMap.get(attrVal).get(outVal).get() ))/((double) sum);
        }
        
        return almostZero();
    }
    
    private double almostZero(){
        return 1.0 / (summarisedNoOfEntities+1);
    }
}
