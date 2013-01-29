/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztbd.iwi;

/**
 *
 * @author jjroman
 */
class NBAttributeValue implements Comparable<NBAttributeValue> {

    private int value;
    public NBAttributeValue(int value ) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    
    @Override
    public boolean equals(Object o){
        return (o instanceof NBAttributeValue) && ((NBAttributeValue)o).getValue() == this.getValue();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.value;
        return hash;
    }
    @Override
    public int compareTo(NBAttributeValue o) {
        return (this.getValue() - o.getValue());
    }
    
    
    
}
