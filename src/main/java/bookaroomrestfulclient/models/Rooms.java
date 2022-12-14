package bookaroomrestfulclient.models;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * @author Team BookARoom
 */

@XmlRootElement
public class Rooms {
    
    private Integer roomId;
    private String roomName;
    private Double roomPrice;
    private String description;
    private String roomType;
    
    public Rooms() {
    }
    

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
}
