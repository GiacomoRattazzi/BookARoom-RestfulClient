package bookaroomrestfulclient.client;

import bookaroomrestfulclient.exceptions.AlreadyExistsException;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
import bookaroomrestfulclient.models.Comments;
//import ch.unil.doplab.grocerystorerestfulclient.models.Foods;
import bookaroomrestfulclient.models.Users;
import bookaroomrestfulclient.models.Rooms;
import bookaroomrestfulclient.models.Dates;
import bookaroomrestfulclient.models.Reservations;
import java.util.List;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import static javax.ws.rs.client.Entity.xml;
import javax.ws.rs.client.WebTarget;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * @author Team BookARoom
 */

public class PersistenceClient {

    
    private static final String USERS_URL = "http://localhost:8080/BookARoom-RestfulService/resources/bookaroomrestfulservice.models.users";
    private static final String ROOMS_URL = "http://localhost:8080/BookARoom-RestfulService/resources/bookaroomrestfulservice.models.rooms";
    private static final String DATES_URL = "http://localhost:8080/BookARoom-RestfulService/resources/bookaroomrestfulservice.models.dates";
    private static final String RESERVATIONS_URL = "http://localhost:8080/BookARoom-RestfulService/resources/bookaroomrestfulservice.models.reservations";
    private static final String COMMENTS_URL = "http://localhost:8080/BookARoom-RestfulService/resources/bookaroomrestfulservice.models.comments";
    
    private static Client client;
    private static WebTarget target;
    private static PersistenceClient instance;

    private PersistenceClient() {
        PersistenceClient.client = ClientBuilder.newClient();
        
    }

    public static PersistenceClient getInstance() {
        if (instance == null) {
            instance = new PersistenceClient();
        }
        return instance;
    }
    
    //USERS

    public Users checkPassword(String username, int password) throws DoesNotExistException {
        Users u = getUsersByName(username);
        if (u.getUsername().equals(username) & u.getPassword() == password) {
            return u;
        }
        throw new DoesNotExistException("Users " + username + " does not exist.");
    }

    public boolean emailExists(String email) throws AlreadyExistsException {
        return client.target(USERS_URL + "/emailExists/" + email).request().get().readEntity(Boolean.class);
    }

    public void createUser(Users user) {
        client.target(USERS_URL + "/create").request().post(Entity.entity(user, "application/xml"));
    }

    public void updateUser(Users user) {
        client.target(USERS_URL + "/edit/" + user.getUserId()).request().put(Entity.entity(user, "application/xml"));
    }

    public void removeUser(int id) {
        client.target(USERS_URL + "/remove/" + id).request().get().readEntity(String.class);
    }

    public Users getUserById(int id) {
        return parseUser(client.target(USERS_URL + "/find/" + id).request().get().readEntity(String.class));
    }

    public Users getUsersByName(String username) {
        Users u = parseUser(client.target(USERS_URL + "/findByName/" + username).request().get(String.class));
        return u;
    }

    public List<Users> getAllUsers() {
        return parseUserList(client.target(USERS_URL).request().get(String.class));
    }

    private List<Users> parseUserList(String xml) {
        List<Users> userList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("users");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Users user = new Users();
            user.setCcnumber(e.getElementsByTagName("ccnumber").item(0).getTextContent());
            user.setCccode(e.getElementsByTagName("cccode").item(0).getTextContent());
            //user.setCcexpirationdate(e.getElementsByTagName("ccexpirationdate").item(0).getTextContent()));
            user.setEmail(e.getElementsByTagName("email").item(0).getTextContent());
            user.setFirstName(e.getElementsByTagName("firstName").item(0).getTextContent());
            user.setLastName(e.getElementsByTagName("lastName").item(0).getTextContent());
            user.setPassword(Integer.valueOf(e.getElementsByTagName("password").item(0).getTextContent()));
            user.setUserId(Integer.valueOf(e.getElementsByTagName("userId").item(0).getTextContent()));
            user.setUsername(e.getElementsByTagName("username").item(0).getTextContent());

            userList.add(user);
        }
        return userList;
    }

    private Users parseUser(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("users").item(0);

        Users user = new Users();
        user.setCcnumber(e.getElementsByTagName("ccnumber").item(0).getTextContent());
        user.setCccode(e.getElementsByTagName("cccode").item(0).getTextContent());
        //user.setCcexpirationdate(e.getElementsByTagName("ccexpirationdate").item(0).getTextContent()));
        user.setEmail(e.getElementsByTagName("email").item(0).getTextContent());
        user.setFirstName(e.getElementsByTagName("firstName").item(0).getTextContent());
        user.setLastName(e.getElementsByTagName("lastName").item(0).getTextContent());
        user.setPassword(Integer.valueOf(e.getElementsByTagName("password").item(0).getTextContent()));
        user.setUserId(Integer.valueOf(e.getElementsByTagName("userId").item(0).getTextContent()));
        user.setUsername(e.getElementsByTagName("username").item(0).getTextContent());

        return user;
    }
    
    
    //ROOMS
    
    public List<Rooms> getAllRooms() {
        return parseRoomList(client.target(ROOMS_URL).request().get(String.class));
    }
    
    public Rooms getCurrentRoom(String roomName) {
        List<Rooms> r = parseRoomList(client.target(ROOMS_URL + "/findByRoomName/" + roomName).request().get(String.class));
            return r.get(0);
    }
    
    private List<Rooms> parseRoomList(String xml) {
        List<Rooms> roomList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("rooms");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Rooms room = new Rooms();
            room.setRoomId(Integer.valueOf(e.getElementsByTagName("roomId").item(0).getTextContent()));
            room.setRoomName(e.getElementsByTagName("roomName").item(0).getTextContent());
            room.setRoomPrice(Double.valueOf(e.getElementsByTagName("roomPrice").item(0).getTextContent()));
            room.setDescription(e.getElementsByTagName("description").item(0).getTextContent());
            room.setRoomType(e.getElementsByTagName("roomType").item(0).getTextContent());
            

            roomList.add(room);
        }
        return roomList;
    }
    

    //RESERVATIONS
    
    public List<Reservations> getAllReservations() {
        return parseReservationsList(client.target(RESERVATIONS_URL).request().get(String.class));
    }
    
    private List<Reservations> parseReservationsList(String xml) {
        List<Reservations> reservationList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("reservations");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Reservations reservation = new Reservations();
            reservation.setReservationId(Integer.valueOf(e.getElementsByTagName("reservationId").item(0).getTextContent()));
            reservation.setReservationNumber(Integer.valueOf(e.getElementsByTagName("reservationNumber").item(0).getTextContent()));
            reservation.setRoomName(e.getElementsByTagName("roomName").item(0).getTextContent());
            reservation.setTotalPrice(Double.valueOf(e.getElementsByTagName("totalPrice").item(0).getTextContent()));
            reservation.setDateArrival(e.getElementsByTagName("dateArrival").item(0).getTextContent());
            reservation.setDateDeparture(e.getElementsByTagName("dateDeparture").item(0).getTextContent());
            

            reservationList.add(reservation);
        }
        return reservationList;
    }
    
    private Reservations parseReservation(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("reservations").item(0);

        Reservations reservation = new Reservations();
        reservation.setReservationId(Integer.valueOf(e.getElementsByTagName("reservationId").item(0).getTextContent()));
        reservation.setReservationNumber(Integer.valueOf(e.getElementsByTagName("reservationNumber").item(0).getTextContent()));
        reservation.setRoomName(e.getElementsByTagName("roomName").item(0).getTextContent());
        reservation.setTotalPrice(Double.valueOf(e.getElementsByTagName("totalPrice").item(0).getTextContent()));
        reservation.setDateArrival(e.getElementsByTagName("dateArrival").item(0).getTextContent());
        reservation.setDateDeparture(e.getElementsByTagName("dateDeparture").item(0).getTextContent());

        return reservation;
    }
    
   public void createReservation(Reservations reservation) {
        client.target(RESERVATIONS_URL + "/create").request().post(Entity.entity(reservation, "application/xml"));
        System.out.println("a");
    }    
   
   public Reservations getReservationByNumber(int number) {
        Reservations r = parseReservation(client.target(RESERVATIONS_URL + "/findByReservationNumber/" + number).request().get(String.class));
            return r;
   }

   
   public void addToHasReservations(int uId, int rId) {
       client.target(USERS_URL + "/addToReservationUser/" + uId + "/" + rId).request().get();
   }
   
   
    public List<Reservations> getAllResInReservations(int id) {
        return parseReservationsList(client.target(USERS_URL + "/getReservations/" + id).request().get(String.class));
    }
    
    public void removeFromReservations(int uId, int rId) {
        client.target(USERS_URL + "/removeFromReservations/" + uId + "/" + rId).request().get();
    }
    
  
    //DATES
   public List<Dates> getAllDatesByRoomName(String roomName) {
       
        Dates nullFixerDate = new Dates();
        nullFixerDate.setDateId(0);
        nullFixerDate.setRoomName("");
        nullFixerDate.setRoomDate("");
       
        List<Dates> d = parseDateList(client.target(DATES_URL + "/findByRoomName/" + roomName).request().get(String.class));
        if (d.size()> 0) {
            return d;
        } else {
            List<Dates> nullFixer = new ArrayList<Dates>();
            nullFixer.add(nullFixerDate);
            return nullFixer;
        }
    }
    
    private List<Dates> parseDateList(String xml) {
        List<Dates> datesList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("dates");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Dates date = new Dates();
            date.setDateId(Integer.valueOf(e.getElementsByTagName("dateId").item(0).getTextContent()));
            date.setRoomName(e.getElementsByTagName("roomName").item(0).getTextContent());
            date.setRoomDate(e.getElementsByTagName("roomDate").item(0).getTextContent());
  

            datesList.add(date);
        }
        return datesList;
    }
    
    
    public void addDate(Dates date) {
        client.target(DATES_URL + "/create").request().post(Entity.entity(date, "application/xml"));
    }
    
    public void removeFromDates(int dId) {
        client.target(DATES_URL + "/removeFromDates/" + dId).request().get();
    }


    private Document parseDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public List<Comments> getAllComments() {
        return parseCommentList(client.target(COMMENTS_URL).request().get(String.class));
        
    }
    
    public void createComment(Comments comment) {
        client.target(COMMENTS_URL + "/create").request().post(Entity.entity(comment, "application/xml"));
    }
    
    
    private List<Comments> parseCommentList(String xml) {
        
        List<Comments> commentList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("comments");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Comments comment = new Comments();
            comment.setCommentId(Integer.valueOf(e.getElementsByTagName("commentId").item(0).getTextContent()));
            comment.setComment(e.getElementsByTagName("comment").item(0).getTextContent());
            comment.setRating(Integer.valueOf(e.getElementsByTagName("rating").item(0).getTextContent()));
            

            commentList.add(comment);
        }
        return commentList;
    }


}
