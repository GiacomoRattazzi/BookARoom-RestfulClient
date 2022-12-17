package bookaroomrestfulclient.beans;
import bookaroomrestfulclient.client.PersistenceClient;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "apiBean")
@SessionScoped
public class ApiBean implements Serializable {
    
    private String weather = "";
    
    public String getWeather() {
        return PersistenceClient.getInstance().getWeather();
    }
    
    public String getWeather2() {
        return weather;
    }
    
    public void setWeather(String weather) {
        this.weather = weather;
    }
  }

