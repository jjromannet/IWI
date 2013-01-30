/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.gui;

import java.util.HashSet;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import sem2.iwi.crawler.MainTwellowCrawler;
import sem2.iwi.crawler.TwellowCategory;

/**
 *
 * @author jjroman
 */
class TwellowCateryModel extends DefaultTableModel {

    
    
    public TwellowCateryModel(Object[][] data, String[] columnNames) {
        super(data,columnNames);
    }

    public static String[] getColumnNames(){
        return new String[]{"Nazwa", "Liczba użytkowników", "URL"};
    }
    public static Object[][] getData(){
        HashSet<TwellowCategory> hstc = MainTwellowCrawler.getMainCategoryList();
        Object[][] data = new Object[hstc.size()][3];
        int i = 0;
        for (Iterator<TwellowCategory> it = hstc.iterator(); it.hasNext(); i++) {
            TwellowCategory tc = it.next();
            data[i][0] = tc.getName();
            data[i][1] = tc.getUsers();
            data[i][2] = tc.getUrl();
        }
        return data;
    }
    
    @Override
    public boolean isCellEditable(int y, int x){
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Integer.class;
        }
        return String.class;
    }
}
