package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
import bookaroomrestfulclient.models.Rooms;
import bookaroomrestfulclient.models.Users;
import bookaroomrestfulclient.models.Dates;
import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.models.Reservations;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Giac
 */

/* TO connect to service:
getBookedDates() --> DONE
findRoomByNameInTheHotel() --> dont use it?
findRoomPrice() --> DONE
getLatestResNumber() --> DONE
AddResToRes() --> DONE
addDatesBooked() --> DONE
deleteDatesBooked() --> DONE

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
    private List<Dates> DelBookedDates = new ArrayList();
    
    public List<Rooms> getRooms() {
        return PersistenceClient.getInstance().getAllRooms();
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    
    
    public ArrayList<String> getBookedDates() {
        
        try {
            bookedDates = PersistenceClient.getInstance().getAllDatesByRoomName(roomName);
            ArrayList<String> n = new ArrayList<>();
            for (int i = 0; i < bookedDates.size(); i++ )  {
                n.add(bookedDates.get(i).getRoomDate()); 
            }
            if (n.size() > 0) {
                return n;
        }
        } catch (NullPointerException e) {
            ArrayList<String> emptyList = new ArrayList<>();
            emptyList.add("");
            return emptyList;
    }return null;
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
    public double findRoomPrice() {

        Rooms r = new Rooms();
        r = PersistenceClient.getInstance().getCurrentRoom(roomName);
        return r.getRoomPrice();
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
    
    //get Latest Reservation Number
    public int getLatestResNumber() {
        return PersistenceClient.getInstance().getAllReservations().size();
    }
    
    //get all dates between range picked by user
    public List<LocalDate> getDatesBetween() { 
        if(range == null) {
            return null;
        } else {
            long numOfDaysBetween = ChronoUnit.DAYS.between(range.get(0), range.get(1)); 
            return IntStream.iterate(0, i -> i + 1)
              .limit(numOfDaysBetween)
              .mapToObj(i -> range.get(0).plusDays(i))
              .collect(Collectors.toList());
                
        }
    }
    
    //Check if the user inputted dates are available for the current room
    public void dateFor() {
        roomEmpty = true;
        temp2 = "";
        for (LocalDate tempBooked : getDatesBetween()) {
            test1 = tempBooked;
            for (String dateBooked : getBookedDates()) {
                if(test1.toString().equals(dateBooked) == true) {
                    roomEmpty = false;
                    temp2 = dateBooked;
                    break;
                }
            }
        }
        }
   
    //add new reservation + add link in user_has_reservations
    public void addResToRes() {
        Users user = LoginBean.getUserLoggedIn();
        
        Reservations newReservation = new Reservations();
        newReservation.setReservationNumber(getLatestResNumber()+1);
        newReservation.setRoomName(roomName);
        newReservation.setTotalPrice(getTotalPrice());
        newReservation.setDateArrival(range.get(0).toString());
        newReservation.setDateDeparture(range.get(1).toString());
        PersistenceClient.getInstance().createReservation(newReservation);
        
        Reservations r = PersistenceClient.getInstance().getReservationByNumber(newReservation.getReservationNumber());
        PersistenceClient.getInstance().addToHasReservations(user.getUserId(), r.getReservationId());
    }
    
    
    public void addDatesBooked() {
       List<LocalDate> tempdate = getDatesBetween();
       for (int i = 0; i <tempdate.size(); i++) {
           Dates date = new Dates();
           date.setRoomName(roomName);
           date.setRoomDate(tempdate.get(i).toString());
           PersistenceClient.getInstance().addDate(date);
       }
    }
    
   
    public void finish() {
        
        dateFor();
        if (roomEmpty==false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This date is already booked: " + temp2 ));
        }else{
            addResToRes();
            addDatesBooked();
            PrimeFaces.current().ajax().update("form:display");
            PrimeFaces.current().executeScript("PF('dlg').show()");
    }
    }
    
 
    //Get current user Reservations
    public List<Reservations> getResInReservations() {
        Users user = LoginBean.getUserLoggedIn();
        return PersistenceClient.getInstance().getAllResInReservations(user.getUserId());
    }
   
    public int getResNbr(){
        return RemoveResNbr;
    }
    
    public void setResNbr(int Rnbr) {
        RemoveResNbr = Rnbr;
    }
    
    public void removeRoomFromReservations() {
        Users user = LoginBean.getUserLoggedIn();
        List<Reservations> reservationsList = PersistenceClient.getInstance().getAllResInReservations(user.getUserId());
        testDates(RemoveResNbr);
        deleteDatesBooked();
        for (Reservations reservation : reservationsList) {
            if (reservation.getReservationNumber().equals(RemoveResNbr)) {
                PersistenceClient.getInstance().removeFromReservations(user.getUserId(), reservation.getReservationId());
            }
        }
    }
    
    public void testDates(int resnbr) { 
        Users user = LoginBean.getUserLoggedIn();
        List<Reservations> reservationsList = PersistenceClient.getInstance().getAllResInReservations(user.getUserId());
        for (Reservations res : reservationsList) {
            if (res.getReservationNumber() == resnbr) {
                DelArrivalDate = LocalDate.parse(res.getDateArrival());
                DelDepartureDate = LocalDate.parse(res.getDateDeparture());
                DelRoomName = res.getRoomName();
            }
        }
    }
    
    private List<LocalDate> getRangeFromReservations(){
        if(DelArrivalDate == null) {
            return null;
        } else {
            long numOfDaysBetween = ChronoUnit.DAYS.between(DelArrivalDate, DelDepartureDate); 
            return IntStream.iterate(0, i -> i + 1)
              .limit(numOfDaysBetween)
              .mapToObj(i -> DelArrivalDate.plusDays(i))
              .collect(Collectors.toList());
                
        }
    }
    
    public void deleteDatesBooked() {
        Users user = LoginBean.getUserLoggedIn();
        List<LocalDate> deletingdates = getRangeFromReservations();
        try {
            DelBookedDates = PersistenceClient.getInstance().getAllDatesByRoomName(DelRoomName);
            for (int i = 0; i < deletingdates.size(); i++) {
                for (Dates d : DelBookedDates) {
                    System.out.println(d.getRoomDate());
                    if (d.getRoomDate().equals(deletingdates.get(i).toString())) {
                        System.out.println(d.getDateId());
                        PersistenceClient.getInstance().removeFromDates(d.getDateId());  
                }
            }
                
        }
        } catch (NullPointerException e) {
        }
    }
    
    
      
    
    



}
    
    
    
    

