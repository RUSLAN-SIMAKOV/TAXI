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
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Integer.compare;
import static java.lang.Math.absExact;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    @Value("${parameter.car-quantity}")
    public int carQuantity;

    @Value("${parameter.car-position.x}")
    public int positionX;

    @Value("${parameter.car-position.y}")
    public int positionY;
    private final CarRepository carRepository;

    @Override
    public BookingResponse bookCar(BookingRequest request) {
        BookingResponse response = findNearestFreeCarWithDistance(request);
        return response;
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

    private Comparator<Pair<Car, Integer>> getDistanceToCarComparator() {
        return (p1, p2) -> p1.getSecond().equals(p2.getSecond()) ?
                compare(p1.getFirst().getCarId(), p2.getFirst().getCarId()) :
                compare(p1.getSecond(), p2.getSecond());
    }

    private Function<Car, Pair<Car, Integer>> createPairCarDistance(BookingRequest r) {
        return c -> Pair.of(c, absExact(r.source().coordinateX() - c.getPositionX()) +
                absExact(r.source().coordinateY() - c.getPositionY()));
    }

    private BookingResponse createBookingResponse(Pair<Car, Integer> p, BookingRequest r) {
        return new BookingResponse(p.getFirst().getCarId(), p.getSecond() +
                absExact(r.destination().coordinateX() - p.getFirst().getSourceX()) +
                absExact(r.destination().coordinateY() - p.getFirst().getSourceY()));
    }

    private Pair<Car, Integer> bookNearestCar(Pair<Car, Integer> pair, BookingRequest request) {
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
                        int direction = compare(c.getDestinationX(), c.getPositionX());
                        c.setPositionX(c.getPositionX() + direction);
                        if (direction != 0) {
                            return;
                        }
                        direction = compare(c.getDestinationY(), c.getPositionY());
                        c.setPositionY(c.getPositionY() + direction);
                        if (c.getDestinationY().equals(c.getPositionY())) {
                            resetCar(c);
                        }
                    } else {
                        int direction = compare(c.getSourceX(), c.getPositionX());
                        c.setPositionX(c.getPositionX() + direction);
                        if (direction != 0) {
                            return;
                        }
                        direction = compare(c.getSourceY(), c.getPositionY());
                        c.setPositionY(c.getPositionY() + direction);
                        if (c.getSourceY().equals(c.getPositionY())) {
                            c.setWithPassenger(true);
                        }
                    }
                })
                .forEach(carRepository::save);

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
}
