package ruslan.simakov.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ruslan.simakov.taxi.model.BookingRequest;
import ruslan.simakov.taxi.model.BookingResponse;
import ruslan.simakov.taxi.model.Car;
import ruslan.simakov.taxi.model.Coordinates;
import ruslan.simakov.taxi.repository.CarRepository;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Long.compare;
import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    private final CarRepository carRepository;

    @Value("${parameter.car-quantity}")
    private long carQuantity;

    @Value("${parameter.car-position.x}")
    private long positionX;

    @Value("${parameter.car-position.y}")
    private long positionY;

    @Override
    public BookingResponse bookCar(BookingRequest request) {
        return findNearestFreeCarWithDistance(request);
    }

    private BookingResponse findNearestFreeCarWithDistance(BookingRequest r) {
        return getAllCarsStream()
                .filter(not(Car::getIsBooked))
                .map(createPairCarDistance(r))
                .min(getDistanceToCarComparator())
                .map(p -> bookNearestCar(p, r))
                .map(p -> createBookingResponse(p, r))
                .orElse(null);
    }

    private Stream<Car> getAllCarsStream() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false);
    }

    private Comparator<Pair<Car, Long>> getDistanceToCarComparator() {
        return (p1, p2) -> p1.getSecond().equals(p2.getSecond()) ?
                compare(p1.getFirst().getCarId(), p2.getFirst().getCarId()) :
                compare(p1.getSecond(), p2.getSecond());
    }

    private Function<Car, Pair<Car, Long>> createPairCarDistance(BookingRequest r) {
        return c -> Pair.of(c, abs(r.source().coordinateX() - c.getPositionX()) +
                abs(r.source().coordinateY() - c.getPositionY()));
    }

    private BookingResponse createBookingResponse(Pair<Car, Long> p, BookingRequest r) {
        var response = new BookingResponse(p.getFirst().getCarId(), p.getSecond() +
                abs(r.destination().coordinateX() - p.getFirst().getSourceX()) +
                abs(r.destination().coordinateY() - p.getFirst().getSourceY()));
        if (response.totalTime() == 0) {
            finishDriving(p.getFirst());
            carRepository.save(p.getFirst());
        }
        return response;
    }

    private Pair<Car, Long> bookNearestCar(Pair<Car, Long> pair, BookingRequest request) {
        carRepository.save(pair.getFirst()
                .setIsBooked(true)
                .setWithPassenger(isPassengerNearCar(pair.getFirst(), request.source()))
                .setSourceX(request.source().coordinateX())
                .setSourceY(request.source().coordinateY())
                .setDestinationX(request.destination().coordinateX())
                .setDestinationY(request.destination().coordinateY()));
        return pair;
    }

    private boolean isPassengerNearCar(Car car, Coordinates source) {
        return source.coordinateX().equals(car.getPositionX()) && source.coordinateY().equals(car.getPositionY());
    }

    @Override
    public void moveCarsOnOneTimeUnit() {
        getAllCarsStream()
                .filter(Car::getIsBooked)
                .peek(c -> {
                    if (c.getWithPassenger()) {
                        moveOnOneTimeUnitToDestination(c);
                    } else {
                        moveOnOneTimeUnitToSource(c);
                    }
                })
                .forEach(carRepository::save);
    }

    private void moveOnOneTimeUnitToSource(Car c) {
        int direction = compare(c.getSourceX(), c.getPositionX());
        c.setPositionX(c.getPositionX() + direction);
        if (direction != 0 && !Objects.equals(c.getSourceX(), c.getPositionX())) {
            return;
        }
        direction = compare(c.getSourceY(), c.getPositionY());
        c.setPositionY(c.getPositionY() + direction);
        if (c.getSourceY().equals(c.getPositionY())) {
            c.setWithPassenger(true);
        }
    }

    private void moveOnOneTimeUnitToDestination(Car c) {
        int direction = compare(c.getDestinationX(), c.getPositionX());
        c.setPositionX(c.getPositionX() + direction);
        if (direction != 0 && !Objects.equals(c.getDestinationX(), c.getPositionX())) {
            return;
        }
        direction = compare(c.getDestinationY(), c.getPositionY());
        c.setPositionY(c.getPositionY() + direction);
        if (c.getDestinationY().equals(c.getPositionY())) {
            finishDriving(c);
        }
    }

    @Override
    public void resetAllCars() {
        getAllCarsStream().map(this::resetCar).forEach(carRepository::save);
    }

    private Car resetCar(Car c) {
        return c.setIsBooked(false)
                .setWithPassenger(false)
                .setPositionX(positionX)
                .setPositionY(positionY)
                .setDestinationX(null)
                .setDestinationY(null)
                .setSourceX(null)
                .setSourceY(null);
    }

    private Car finishDriving(Car car) {
        return car.setIsBooked(false)
                .setWithPassenger(false)
                .setDestinationX(null)
                .setDestinationY(null)
                .setSourceX(null)
                .setSourceY(null);
    }
}
