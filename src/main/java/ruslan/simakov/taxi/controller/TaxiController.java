package ruslan.simakov.taxi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruslan.simakov.taxi.model.BookingRequest;
import ruslan.simakov.taxi.model.BookingResponse;
import ruslan.simakov.taxi.service.DrivingService;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaxiController {

    private final DrivingService drivingService;

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookCar(@RequestBody BookingRequest request) {
        BookingResponse response = null;
        if (isValidRequest(request)) {
             response = drivingService.bookCar(request);
        }
        log.info("Send response: {} on booking car request: {}", response, request);
        return ResponseEntity.ok(response);
    }

    private boolean isValidRequest(BookingRequest request) {
        return nonNull(request) && nonNull(request.source()) && nonNull(request.destination()) &&
                nonNull(request.source().coordinateX()) && nonNull(request.source().coordinateY()) &&
                nonNull(request.destination().coordinateX()) && nonNull(request.destination().coordinateY()) &&
                request.source().coordinateX() <= MAX_VALUE && request.source().coordinateX() >= MIN_VALUE &&
                request.source().coordinateY() <= MAX_VALUE && request.source().coordinateY() >= MIN_VALUE &&
                request.destination().coordinateX() <= MAX_VALUE && request.destination().coordinateX() >= MIN_VALUE &&
                request.destination().coordinateY() <= MAX_VALUE && request.destination().coordinateY() >= MIN_VALUE;
    }

    @PostMapping("/tick")
    public ResponseEntity<?> driveCar() {
        drivingService.moveCarsOnOneTimeUnit();
        log.info("Made one TimeUnit tick");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetAllCars() {
        drivingService.resetAllCars();
        log.info("Reset all cars");
        return ResponseEntity.ok().build();
    }
}
