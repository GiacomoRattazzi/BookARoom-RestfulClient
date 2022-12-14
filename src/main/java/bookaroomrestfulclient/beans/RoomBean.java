package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
import bookaroomrestfulclient.models.Rooms;
import bookaroomrestfulclient.models.Users;
import bookaroomrestfulclient.models.Dates;
import bookaroomrestfulclient.client.PersistenceClient;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Giac
 */

/* TO connect to service:
getBookedDates()
findRoomByNameInTheHotel() --> dont use it?
findRoomPrice()
getLatestResNumber()
AddResToRes()
addDatesBooked() --> maybe last
deleteDatesBooked() --> maybe last

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
    private List<Dates> bookedDates = new ArrayList();
    
    public List<Rooms> getRooms() {
        return PersistenceClient.getInstance().getAllRooms();
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    
    
    public ArrayList<String> getBookedDates() {
        bookedDates = PersistenceClient.getInstance().getAllDates();
        ArrayList<String> n = new ArrayList<>();
        for (int i = 0; i < bookedDates.size(); i++ )  {
            n.add(bookedDates.get(i).getRoomDate()); 
        }
        return n; 

    }     
    
    
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public String setRoomNameAndRedirect(String roomName) {
        this.roomName = roomName;
        return "/UserPage/Booking.xhtml?faces-redirect=true";
    }
    
    public List<LocalDate> getRange() {
        return range;
    }
 
    public void setRange(List<LocalDate> range) {
        this.range = range;
    }
    
    public LocalDate getToday() {
        return LocalDate.now(); 
    }

    public LocalDate getNextMonth() {
        LocalDate today = LocalDate.now();
        return today.plusMonths(1).withDayOfMonth(1);
    }
    
    
    
    
    
    
    
    
        
    //find price of current room that user is booking
    private double findRoomPrice() {
        return 0;
        /*TODO
        Query query = em.createNamedQuery("Rooms.findByRoomName");
        List<Rooms> rooms = query.setParameter("roomName", roomName).getResultList();
                return rooms.get(0).getRoomPrice();*/
            }

        
       
    //get total price of the room that user is booking (nbr of nights*roomPrice)
    
    public double getTotalPrice() {
        if (range!=null){
            long diffDays = ChronoUnit.DAYS.between(range.get(0), range.get(1));
            totalPrice = diffDays * findRoomPrice();
            System.out.println(totalPrice);
                return totalPrice;
             
        }else{
            return 0;
        }
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
