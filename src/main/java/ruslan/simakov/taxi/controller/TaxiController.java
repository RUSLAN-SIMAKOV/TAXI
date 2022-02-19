package ruslan.simakov.taxi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruslan.simakov.taxi.model.BookingRequest;

@RestController
@RequestMapping("/api")
public class TaxiController {

    @PostMapping("/book")
    public ResponseEntity<BookingRequest> bookCar(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(request);
    }

    @PostMapping("/tick")
    public ResponseEntity<?> driveCar() {
        return ResponseEntity.ok("TICK");
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetAllCars() {
        return ResponseEntity.ok("RESET");
    }
}
