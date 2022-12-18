package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.exceptions.AlreadyExistsException;
import bookaroomrestfulclient.models.Users;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Giac
 */

@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private String ccnumber = "";
    private String cccode = "";
    private Date ccexpirationdate = new Date(System.currentTimeMillis());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
    private final YearMonth CurrentTime = YearMonth.now();
    private final DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDate CurrentTimeLong = LocalDate.now();
    
    public void createAUser()  {
        //try {
            //boolean a = !PersistenceClient.getInstance().emailExists(email);
            //boolean b = PersistenceClient.getInstance().getUsersByName(username) == null;
            //if (a) {
                Users newUser = new Users();
                newUser.setUsername(username);
                newUser.setFirstName(firstName);
                 newUser.setLastName(lastName);
                newUser.setEmail(email);
                newUser.setPassword(password.hashCode());
                newUser.setCcnumber(ccnumber);
                newUser.setCccode(cccode);
                newUser.setCcexpirationdate(ccexpirationdate);
                PersistenceClient.getInstance().createUser(newUser);
                
            //}
        //} catch (AlreadyExistsException ex) {
            //System.out.println(ex.getMessage());
        //}
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
    }
    
    public void modifyAUser(){
        Users user = LoginBean.getUserLoggedIn();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password.hashCode());
        PersistenceClient.getInstance().updateUser(user);
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
        
    public String getCcnumber() {
        return ccnumber;
    }
    
    public String getCccode() {
        return cccode;
    }
    
    public Date getCcexpirationdate() {
        return ccexpirationdate;
    }
    
    public YearMonth getCurrentTime() {
        return CurrentTime;
    }

    public LocalDate getCurrentTimeLong() {
        return CurrentTimeLong;
    }
    
    
    public DateTimeFormatter getFormatter() {
        return formatter;
    }
    
    public DateTimeFormatter getDateFormatter() {
        return Dateformatter;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
        System.out.println(username);
    }
    
    public void setCcnumber(String nccnumber) {
         this.ccnumber = nccnumber;
         System.out.println(ccnumber);
    }
    
    public void setCccode(String ncccode) {
         this.cccode = ncccode;
    }
    
    public void setCcexpirationdate(Date nccexpirationdate) {
        this.ccexpirationdate = nccexpirationdate;
    } 


}