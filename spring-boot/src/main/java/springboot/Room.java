package springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {
    private static final Logger logger = Logger.getLogger(Room.class.getName()); // Logger instance
    private Double cost = 0.00;
    private Integer roomNumber, numOfBeds, qualityLevel = 0;
    private String typeOfRoom, bedType = "";
    private boolean smokingAllowed = false;

    //DEFAULT CONSTRUCTOR
    public Room() {
    }

    //CONSTRUCTOR
    public Room(Integer roomNum, Double c, String roomType, Integer numBed, Integer quality, String bedType, boolean smoking) {
        this.cost = c;
        this.roomNumber = roomNum;
        this.numOfBeds = numBed;
        this.qualityLevel = quality;
        this.typeOfRoom = roomType;
        this.smokingAllowed = smoking;
        this.bedType = bedType;
    }

    public Room(Room room){
        this.cost = room.getCost();
        this.roomNumber = room.getRoomNumber();
        this.numOfBeds = room.getNumOfBeds();
        this.qualityLevel = room.getQualityLevel();
        this.typeOfRoom = room.getTypeOfRoom();
        this.smokingAllowed = room.getSmokingAllowed();
        this.bedType = room.getBedType();
    }

    //Parse Dates
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return sdf.parse(dateString);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing date: " + dateString, e);
            return null;
        }
    }

    //PRINT INFO
    public void printRoomInfo() {
        System.out.println(roomNumber);
        System.out.println("Room type: " + typeOfRoom);
        System.out.println("Bed type: " + bedType);
        System.out.println("# of beds: " + numOfBeds);
        System.out.println("Quality level: " + qualityLevel);
        if (!smokingAllowed) {
            System.out.println("Smoking not allowed.");
        } else {
            System.out.println("Smoking allowed.");
        }
    }
    // Search for available rooms based on criteria
    //the two strings at the end are in the format: 2024-04-20T20:39:06.000Z
    public Room findRoom(int roomNumber) throws IOException {
        Reservation r = new Reservation();
        List<Room> rooms = r.readInAllRooms();
        //so now we will check all rooms only rooms that match the desired criteria
        // if room does NOT match criteria, remove it from list
        rooms.removeIf(curr -> curr.getRoomNumber() != roomNumber);

        //at the end of this loop, the list rooms should only contain the desired room

        if(rooms.size() == 1){
            return rooms.get(0);
        }
        else{
            return null;
        }
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getNumOfBeds() {
        return numOfBeds;
    }

    public void setNumOfBeds(Integer numOfBeds) {
        this.numOfBeds = numOfBeds;
    }

    public Integer getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(Integer qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public String getTypeOfRoom() {
        return typeOfRoom;
    }

    public void setTypeOfRoom(String typeOfRoom) {
        this.typeOfRoom = typeOfRoom;
    }

    public boolean getSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public String getBedType(){
        return bedType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return getSmokingAllowed() == room.getSmokingAllowed() &&
                getRoomNumber().equals(room.getRoomNumber()) &&
                getNumOfBeds().equals(room.getNumOfBeds()) &&
                getBedType().equalsIgnoreCase(room.getBedType())&&
                getTypeOfRoom().equalsIgnoreCase(room.getTypeOfRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCost(), getRoomNumber(), getNumOfBeds(), getQualityLevel(), getTypeOfRoom(), getTypeOfRoom(), smokingAllowed);
    }
}
