package ruslan.simakov.taxi.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ruslan.simakov.taxi.model.Car;
import ruslan.simakov.taxi.repository.CarRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarCreationServiceTest {

    @Mock
    CarRepository carRepository;

    CarCreationService carCreationService;

    @SneakyThrows
    @BeforeEach
    void init() {
        carCreationService = new CarCreationService(carRepository);
        Field field = carCreationService.getClass().getDeclaredField("carQuantity");
        field.setAccessible(true);
        field.set(carCreationService, 3);
    }

    @SneakyThrows
    @Test
    void createInitNumberOfCarsTest() {
        Method method = carCreationService.getClass().getDeclaredMethod("createInitNumberOfCars");
        method.setAccessible(true);

        method.invoke(carCreationService);

        verify(carRepository, times(3)).save(any(Car.class));
    }
}
