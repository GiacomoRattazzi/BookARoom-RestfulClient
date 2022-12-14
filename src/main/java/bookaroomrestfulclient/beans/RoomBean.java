package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
import bookaroomrestfulclient.models.Rooms;
import bookaroomrestfulclient.models.Users;
import bookaroomrestfulclient.client.PersistenceClient;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Giac
 */

@Named(value = "roomBean")
@SessionScoped
public class RoomBean implements Serializable {

    private String roomName = "";
    private double totalPrice = 0;
    private List<LocalDate> range = null;
    private List<LocalDate> betweenRange;
    private LocalDate test1;
    private Boolean roomEmpty = true;
    private String temp2;
    
    private int resNbr = 0;
    private int RemoveResNbr = 0;
    private String DelRoomName = "";
    private LocalDate DelArrivalDate;
    private LocalDate DelDepartureDate;
    
    public List<Rooms> getRooms() {
        return PersistenceClient.getInstance().getAllRooms();
        
    }
    
    
}
