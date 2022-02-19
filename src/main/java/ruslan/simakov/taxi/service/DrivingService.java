package ruslan.simakov.taxi.service;

import ruslan.simakov.taxi.model.BookingRequest;
import ruslan.simakov.taxi.model.BookingResponse;

public interface DrivingService {

    BookingResponse bookCar(BookingRequest request);

    void moveCarsOnOneTimeUnit();

    void resetAllCars();
}
