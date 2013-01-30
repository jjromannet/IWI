/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.crawler;

/**
 *
 * @author jjroman
 */
public class TwellowCategory {
    private String name;
    private String url;
    private int users;

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
