package bookaroomrestfulclient.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Team BookARoom
 */

@XmlRootElement
public class Dates {
    
    private Integer dateId;
    private String roomName;
    private String roomDate;
    
    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }
    
}
