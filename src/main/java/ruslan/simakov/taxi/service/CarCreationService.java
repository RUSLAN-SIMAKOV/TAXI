package ruslan.simakov.taxi.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ruslan.simakov.taxi.model.Car;
import ruslan.simakov.taxi.repository.CarRepository;

import static java.util.stream.Stream.generate;

@Service
@RequiredArgsConstructor
public class CarCreationService {

    @Value("${parameter.car-quantity}")
    public long carQuantity;

    @Value("${parameter.car-position.x}")
    public long positionX;

    @Value("${parameter.car-position.y}")
    public long positionY;

    private final CarRepository carRepository;

    @PostConstruct
    private void createInitNumberOfCars() {
        generate(Car::new)
                .limit(carQuantity)
                .map(car -> car.setIsBooked(false)
                        .setWithPassenger(false)
                        .setPositionX(positionX)
                        .setPositionY(positionY))
                .forEach(carRepository::save);
    }
}
