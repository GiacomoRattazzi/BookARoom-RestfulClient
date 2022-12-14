package bookaroomrestfulclient.models;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;


/*
 * @author Team BookARoom
 */




@XmlRootElement
public class Users {
    
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer password;
    private String ccnumber;
    private String cccode;
    private Date ccexpirationdate;
    //Error, will fix  when adding reservations class
    //private List<Reservations> reservationsList;
    
    public Users() {
    }
        
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public String getCcnumber() {
        return ccnumber;
    }

    public void setCcnumber(String ccnumber) {
        this.ccnumber = ccnumber;
    }

    public String getCccode() {
        return cccode;
    }

    public void setCccode(String cccode) {
        this.cccode = cccode;
        
    }

    public Date getCcexpirationdate() {
        return ccexpirationdate;
    }

    public void setCcexpirationdate(Date ccexpirationdate) {
        this.ccexpirationdate = ccexpirationdate;
    }
}
