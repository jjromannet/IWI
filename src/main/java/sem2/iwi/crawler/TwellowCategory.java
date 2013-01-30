/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.crawler;

import java.util.Objects;

/**
 *
 * @author jjroman
 */
public class TwellowCategory {
    private String name;
    private String url;
    private int users;

    @Override
    public boolean equals(Object o){
        return (o instanceof TwellowCategory) && ((TwellowCategory)o).getName().equals(name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }
    public TwellowCategory(String name, String url, int users) {
        this.name = name;
        this.url = url;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }
    
}
