package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
import bookaroomrestfulclient.models.Users;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/*
 * @author Team BookARoom
 */

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static Users currentUser;
    private String username = "";
    private String password = "";
    
    public String userLogsIn() {
        try {
            Users u = PersistenceClient.getInstance().checkPassword(username, password.hashCode());
            if (u != null) {
                currentUser = u;
                return "/UserPage/UserMainPage.xhtml?faces-redirect=true";
            }
        } catch (DoesNotExistException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return "/MainPage/LoginPage.xhtml?faces-redirect=true";
    }
    
    public String userLogsout() {
        currentUser = null;
        return "/MainPage/MainPage.xhtml?faces-redirect=true";
    }

    public static Users getUserLoggedIn() {
        return currentUser;
    }
    
    public static void setUserLoggedIn(Users user) {
        currentUser = user;
    }
    public Users getCurrentUser() {
        return currentUser;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}