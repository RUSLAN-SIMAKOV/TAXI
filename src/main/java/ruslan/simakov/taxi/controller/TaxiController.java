package ruslan.simakov.taxi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaxiController {

    @PostMapping("/book")
    public ResponseEntity<?> bookCar() {
        return ResponseEntity.ok("BOOK");
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
