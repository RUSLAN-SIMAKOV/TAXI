package ruslan.simakov.taxi.util;

import ruslan.simakov.taxi.model.BookingRequest;
import ruslan.simakov.taxi.model.Car;
import ruslan.simakov.taxi.model.Coordinates;

public class TestFactory {

    public static long MINUS_ONE = -1L;
    public static long ZERO = 0L;
    public static long ONE = 1L;
    public static long TWO = 2L;
    public static long THREE = 3L;
    public static long FOUR = 4L;
    public static long SIX = 6L;
    public static long EIGHT = 8L;
    public static long INTEGER_MIN_VALUE = -2147483648L;
    public static long INTEGER_MAX_VALUE = 2147483647L;
    public static String BOOKING_REQUEST = """
            {"source": {"x": -1, "y": -1}, "destination": {"x": 1, "y": 1}}""";
    public static String REQUEST_FOR_ONE_TICK = """
            {"source": {"x": 0, "y": 0}, "destination": {"x": 1, "y": 0}}""";
    public static String REQUEST_FOR_BOOKING_FIRST_CAR = """
            {"source": {"x": 1, "y": 0}, "destination": {"x": 2, "y": 0}}""";
    public static String RESPONSE_FOR_FIRST_CAR = """
            {"car_id":1,"total_time":6}""";
    public static String RESPONSE_FOR_FIRST_CAR_IN_ONE_TICK = """
            {"car_id":1,"total_time":1}""";
    public static String RESPONSE_FOR_SECOND_CAR = """
            {"car_id":2,"total_time":6}""";
    public static String RESPONSE_FOR_THIRD_CAR = """
            {"car_id":3,"total_time":6}""";

    public static Car createCar() {
        return new Car()
                .setIsBooked(false)
                .setWithPassenger(false)
                .setPositionX(ZERO)
                .setPositionY(ZERO)
                .setSourceX(ZERO)
                .setSourceY(ZERO)
                .setDestinationX(ZERO)
                .setDestinationY(ZERO);
    }

    public static BookingRequest createBookingRequest() {
        return new BookingRequest(new Coordinates(MINUS_ONE, MINUS_ONE), new Coordinates(ONE, ONE));
    }

    public static BookingRequest createBookingRequestWithSameSourceAndDestination() {
        return new BookingRequest(new Coordinates(MINUS_ONE, MINUS_ONE), new Coordinates(MINUS_ONE, MINUS_ONE));
    }

    public static BookingRequest createLongDriveBookingRequest() {
        return new BookingRequest(new Coordinates(INTEGER_MIN_VALUE, INTEGER_MIN_VALUE),
                new Coordinates(INTEGER_MAX_VALUE, INTEGER_MAX_VALUE));
    }
}
