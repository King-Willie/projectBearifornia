package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.Room;
import springboot.Reservation;
import springboot.service.ReservationService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

//    @GetMapping("/search")
//    public ResponseEntity<List<Room>> searchAvailableRooms(
//            @RequestParam String smoking,
//            @RequestParam String bedType,
//            @RequestParam String numOfBeds,
//            @RequestParam String roomType,
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//
//        List<Room> rooms = reservationService.searchAvailableRooms(smoking, bedType, numOfBeds, roomType, startDate, endDate);
//        if (rooms.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(rooms);
//    }

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        String result = reservationService.createReservation(reservation);
        if ("success".equals(result)) {
            return ResponseEntity.ok("Reservation created successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to create reservation.");
        }
    }


    @PostMapping("/checkForRooms")
    public ResponseEntity<Room> checkAvailability(@RequestBody String[] payload) throws IOException {
        int bedNum;
        if(payload[1].equals("single")){
            bedNum = 1;
        }
        else if(payload[1].equals("double")){
            bedNum = 2;
        }
        else{
            bedNum = 3;
        }
        Reservation r = new Reservation();
        ArrayList<Room> availableRooms = (ArrayList<Room>) r.searchRooms(
                            Boolean.parseBoolean(payload[0]),
                payload[1],bedNum,payload[2],
                payload[3], payload[4]);

        if(availableRooms.isEmpty()){
            return ResponseEntity.badRequest().body(new Room());
        }
        else{
            return ResponseEntity.ok(availableRooms.get(0));
        }
    }
}
