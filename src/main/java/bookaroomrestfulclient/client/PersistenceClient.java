package bookaroomrestfulclient.client;

import bookaroomrestfulclient.exceptions.AlreadyExistsException;
import bookaroomrestfulclient.exceptions.DoesNotExistException;
//import ch.unil.doplab.grocerystorerestfulclient.models.Foods;
import bookaroomrestfulclient.models.Users;
import bookaroomrestfulclient.models.Rooms;
import bookaroomrestfulclient.models.Dates;
import java.util.List;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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

    
    private static final String USERS_URL = "http://localhost:8080/BookARoomRestfulService/resources/bookaroomrestfulservice.models.users";
    private static final String ROOMS_URL = "http://localhost:8080/BookARoomRestfulService/resources/bookaroomrestfulservice.models.rooms";
    private static final String DATES_URL = "http://localhost:8080/BookARoomRestfulService/resources/bookaroomrestfulservice.models.dates";

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
    
    //DATES
    
    public List<Dates> getAllDates() {
        return parseDateList(client.target(DATES_URL).request().get(String.class));
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

}
