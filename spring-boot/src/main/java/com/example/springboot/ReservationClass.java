package com.example.springboot;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationClass {

    public Room room;
    private static Integer idNumber;
    private static String name;
    private static LocalDate startDay;
    private static LocalDate endDay;
    private static Integer price;

    public ReservationClass(Integer id, String n) {
        idNumber = id;
        name = n;
    }

    public ReservationClass(Integer id, String n, LocalDate start, LocalDate end) {
        idNumber = id;
        name = n;
        startDay = start;
        endDay = end;
    }

    //returns the String that was removed from the csv file (commas included)
    //or it returns failure
    public String removeAvailableRoom(Room reservedRoom) throws IOException {
        ArrayList<Room> availableRoomList = new ArrayList<>(); //store all the rooms we read in
        InputStream is = this.getClass().getResourceAsStream("/RoomsAvailable.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        reader.readLine(); //skip first line of header info
        String line;
        //store all the lines in the csv
        List<String> availableRoomsLines = new ArrayList<>();

        //read in available rooms from csv and store in list
        while ((line = reader.readLine()) != null) {
            availableRoomsLines.add(line);

            String[] split = line.split(",");
            Room currentRoom = new Room(Double.parseDouble(split[1]),//cost
                    Integer.parseInt(split[0]), //roomNumber
                    Integer.parseInt(split[3]), //number of beds
                    Integer.parseInt(split[4]), //quality level
                    split[2], //roomType
                    split[6].equals("Y") //smoking
            );
            currentRoom.setBedType(split[5]); //bedType

            availableRoomList.add(currentRoom);
        }

        int indexRemove = availableRoomList.indexOf(reservedRoom);

        // Read and write lines, skipping the one to remove
        if(indexRemove >= 0 && indexRemove < availableRoomsLines.size()){
            String removedLine = availableRoomsLines.get(indexRemove);
            availableRoomsLines.remove(indexRemove);
            FileWriter fw = new FileWriter("RoomsTaken.csv");
            try(BufferedWriter writer = new BufferedWriter(fw)){
                for (String l : availableRoomsLines){
                    writer.write(l);
                    writer.newLine();
                }
                //overwrite the last line in the csv which is now a duplicate
                writer.write("");
                writer.newLine();
                return removedLine;
            }
            catch(IOException e){
                e.printStackTrace();
                return "failure, could not modify and write to the RoomsTaken database";
            }
        }
        else{ //if the room to remove wasn't found in the available Rooms
            return "failure , room to reserve does not exist";
        }
    }

    //returns either a failure message or "success"
    public String reserveRoom(Room reservedRoom){
        String removedRoom;
        try {
            //attempt removing the available room from the RoomsAvailable.csv
            removedRoom = removeAvailableRoom(reservedRoom);
        }
        catch(IOException x){
            x.printStackTrace();
            return "failed to remove room from database of available rooms";
        }
        if(! removedRoom.contains("failure")){
            String reserveRoom = addReservedRoom(removedRoom);
            if(reserveRoom.equals("success")) {
                return "success";
            }
        }
        return removedRoom;
    }

    //takes a csv formatted line and puts it into the RoomsTaken.csv
    //returns either success" or a fail message depending on result
    public String addReservedRoom(String newRoom){
        InputStream is = this.getClass().getResourceAsStream("/RoomsTaken.csv");

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to read in the currently reserved rooms";
        }

        // Add the new line containing the new reservation
        if(! lines.contains(newRoom)) {
            lines.add(newRoom);
        }
        else{
            return "Room is already reserved";
        }

        FileWriter fw;
        try {
            fw = new FileWriter("RoomsTaken.csv");
        }
        catch(IOException x){
            x.printStackTrace();
            return "Could not write to RoomsTaken database";
        }

        // Write the updated content back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(fw))
        {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Could not write to RoomsTaken database";
        }

        return "success";
    }

    public static LocalDate getStartDay() {
        return startDay;
    }

    public static void setStartDay(LocalDate startDay) {
        ReservationClass.startDay = startDay;
    }

    public static LocalDate getEndDay() {
        return endDay;
    }

    public static void setEndDay(LocalDate endDay) {
        ReservationClass.endDay = endDay;
    }

    public static Integer getPrice() {
        return price;
    }

    public static void setPrice(Integer price) {
        ReservationClass.price = price;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReservationClass) {
            return ((ReservationClass) obj).getIdNumber().equals(idNumber) &&
                    ((ReservationClass) obj).getName().equals(name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, idNumber, endDay, startDay);
    }
}

