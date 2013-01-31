/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.bayes;

import java.util.Objects;

/**
 *
 * @author jjroman
 */
public class NBOutputValue {
    int key = 0;
    String name = null;

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public NBOutputValue(String key, String name) {
        this(Integer.parseInt(key), name);
    }
    
    public NBOutputValue(int key, String name) {
        this.key = key;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof NBOutputValue) && ((NBOutputValue)o).hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.key;
        hash = 19 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
