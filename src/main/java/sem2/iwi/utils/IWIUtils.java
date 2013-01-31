/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kubeusz
 */
public class IWIUtils {
    
    public static String getPropertyValue(String propertyName){
        
        Properties prop = new Properties();
        try {
            prop.load(IWIUtils.class.getClassLoader().getResourceAsStream("twitter4j.properties"));
        } catch (IOException ex) {
            Logger.getLogger(IWIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return prop.getProperty(propertyName);
        
    }
    
    public static void setPropertyValue(String propertyName, String propertyValue){
        
        Properties prop = new Properties();
        
        try {
            prop.load(IWIUtils.class.getClassLoader().getResourceAsStream("twitter4j.properties"));
        } catch (IOException ex) {
            Logger.getLogger(IWIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        prop.setProperty(propertyName, propertyValue);
        
    }
    
}
