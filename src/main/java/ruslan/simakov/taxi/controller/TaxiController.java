package ruslan.simakov.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruslan.simakov.taxi.model.BookingRequest;
import ruslan.simakov.taxi.model.BookingResponse;
import ruslan.simakov.taxi.service.DrivingService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaxiController {

    private final DrivingService drivingService;

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookCar(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(drivingService.bookCar(request));
    }

    @PostMapping("/tick")
    public ResponseEntity<?> driveCar() {
        drivingService.moveCarsOnOneTimeUnit();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetAllCars() {
        drivingService.resetAllCars();
        return ResponseEntity.ok().build();
    }
}
