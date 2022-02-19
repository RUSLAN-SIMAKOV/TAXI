package ruslan.simakov.taxi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ruslan.simakov.taxi.repository.CarRepository;
import ruslan.simakov.taxi.util.TestFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ruslan.simakov.taxi.util.TestFactory.EIGHT;
import static ruslan.simakov.taxi.util.TestFactory.FOUR;
import static ruslan.simakov.taxi.util.TestFactory.INTEGER_MAX_VALUE;
import static ruslan.simakov.taxi.util.TestFactory.MINUS_ONE;
import static ruslan.simakov.taxi.util.TestFactory.ONE;
import static ruslan.simakov.taxi.util.TestFactory.SIX;
import static ruslan.simakov.taxi.util.TestFactory.THREE;
import static ruslan.simakov.taxi.util.TestFactory.TWO;
import static ruslan.simakov.taxi.util.TestFactory.ZERO;
import static ruslan.simakov.taxi.util.TestFactory.createBookingRequest;
import static ruslan.simakov.taxi.util.TestFactory.createBookingRequestWithSameSourceAndDestination;
import static ruslan.simakov.taxi.util.TestFactory.createCar;
import static ruslan.simakov.taxi.util.TestFactory.createLongDriveBookingRequest;

@ExtendWith(MockitoExtension.class)
class DrivingServiceImplTest {

    @Mock
    CarRepository carRepository;
    @InjectMocks
    DrivingServiceImpl drivingService;

    @Test
    void bookCarTest() {
        var bookingRequest = createBookingRequest();
        AtomicLong id = new AtomicLong(1L);
        when(carRepository.findAll()).thenReturn(generate(TestFactory::createCar)
                .limit(3)
                .map(c -> c.setCarId(id.getAndIncrement()))
                .toList());

        var bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("totalTime", SIX);

        bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", TWO)
                .hasFieldOrPropertyWithValue("totalTime", SIX);

        bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", THREE)
                .hasFieldOrPropertyWithValue("totalTime", SIX);

        bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).isNull();
    }

    @Test
    void bookLongDriveCarTest() {
        var bookingRequest = createLongDriveBookingRequest();
        var car = createCar()
                .setCarId(ONE)
                .setPositionX(INTEGER_MAX_VALUE)
                .setPositionY(INTEGER_MAX_VALUE);

        when(carRepository.findAll()).thenReturn(List.of(car));

        var bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("totalTime", 17179869180L);
    }

    @Test
    void bookNearestCarTest() {
        var bookingRequest = createBookingRequest();
        var car = createCar()
                .setCarId(ONE)
                .setPositionX(INTEGER_MAX_VALUE)
                .setPositionY(INTEGER_MAX_VALUE);

        var carNearest = createCar()
                .setCarId(TWO)
                .setPositionX(ONE)
                .setPositionY(ONE);

        when(carRepository.findAll()).thenReturn(List.of(car, carNearest));

        var bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", TWO)
                .hasFieldOrPropertyWithValue("totalTime", EIGHT);
    }

    @Test
    void bookCarInTheSamePointTest() {
        var bookingRequest = createBookingRequest();
        var car = createCar()
                .setCarId(ONE)
                .setPositionX(MINUS_ONE)
                .setPositionY(MINUS_ONE);

        when(carRepository.findAll()).thenReturn(List.of(car));

        var bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("totalTime", FOUR);
    }

    @Test
    void bookCarWithTheSamePositionAndSourceAndDestinationTest() {
        var bookingRequest = createBookingRequestWithSameSourceAndDestination();
        var car = createCar()
                .setCarId(ONE)
                .setPositionX(MINUS_ONE)
                .setPositionY(MINUS_ONE);

        when(carRepository.findAll()).thenReturn(List.of(car));

        var bookingResponse = drivingService.bookCar(bookingRequest);
        assertThat(bookingResponse).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("totalTime", ZERO);
        assertThat(car.getIsBooked()).isFalse();
        assertThat(car.getWithPassenger()).isFalse();
    }

    @Test
    void moveCarsOnOneTimeUnitTest() {
        var car = createCar()
                .setCarId(ONE)
                .setDestinationX(ONE)
                .setIsBooked(true)
                .setWithPassenger(true);
        var car2 = createCar()
                .setCarId(TWO)
                .setDestinationY(ONE)
                .setIsBooked(true)
                .setWithPassenger(true);

        when(carRepository.findAll()).thenReturn(List.of(car, car2));

        drivingService.moveCarsOnOneTimeUnit();

        assertThat(car).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("isBooked", false)
                .hasFieldOrPropertyWithValue("withPassenger", false)
                .hasFieldOrPropertyWithValue("positionX", ONE)
                .hasFieldOrPropertyWithValue("positionY", ZERO)
                .hasFieldOrPropertyWithValue("sourceX", null)
                .hasFieldOrPropertyWithValue("sourceY", null)
                .hasFieldOrPropertyWithValue("destinationX", null)
                .hasFieldOrPropertyWithValue("destinationY", null);

        assertThat(car2).hasFieldOrPropertyWithValue("carId", TWO)
                .hasFieldOrPropertyWithValue("isBooked", false)
                .hasFieldOrPropertyWithValue("withPassenger", false)
                .hasFieldOrPropertyWithValue("positionX", ZERO)
                .hasFieldOrPropertyWithValue("positionY", ONE)
                .hasFieldOrPropertyWithValue("sourceX", null)
                .hasFieldOrPropertyWithValue("sourceY", null)
                .hasFieldOrPropertyWithValue("destinationX", null)
                .hasFieldOrPropertyWithValue("destinationY", null);
    }

    @Test
    void resetAllCarsTest() {
        var car = createCar()
                .setCarId(ONE)
                .setPositionX(ONE)
                .setPositionY(ONE)
                .setIsBooked(true)
                .setWithPassenger(true);
        var car2 = createCar()
                .setCarId(TWO)
                .setPositionX(TWO)
                .setPositionY(TWO)
                .setIsBooked(true)
                .setWithPassenger(true);
        when(carRepository.findAll()).thenReturn(List.of(car, car2));

        drivingService.resetAllCars();

        assertThat(car).hasFieldOrPropertyWithValue("carId", ONE)
                .hasFieldOrPropertyWithValue("isBooked", false)
                .hasFieldOrPropertyWithValue("withPassenger", false)
                .hasFieldOrPropertyWithValue("positionX", ZERO)
                .hasFieldOrPropertyWithValue("positionY", ZERO)
                .hasFieldOrPropertyWithValue("sourceX", null)
                .hasFieldOrPropertyWithValue("sourceY", null)
                .hasFieldOrPropertyWithValue("destinationX", null)
                .hasFieldOrPropertyWithValue("destinationY", null);

        assertThat(car2).hasFieldOrPropertyWithValue("carId", TWO)
                .hasFieldOrPropertyWithValue("isBooked", false)
                .hasFieldOrPropertyWithValue("withPassenger", false)
                .hasFieldOrPropertyWithValue("positionX", ZERO)
                .hasFieldOrPropertyWithValue("positionY", ZERO)
                .hasFieldOrPropertyWithValue("sourceX", null)
                .hasFieldOrPropertyWithValue("sourceY", null)
                .hasFieldOrPropertyWithValue("destinationX", null)
                .hasFieldOrPropertyWithValue("destinationY", null);
    }
}
