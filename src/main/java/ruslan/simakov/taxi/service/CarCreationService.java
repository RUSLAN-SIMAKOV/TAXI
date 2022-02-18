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

    @Value("${parameter.car-quantity:3}")
    private int carQuantity;

    @Value("${parameter.car-position.x:0}")
    private int positionX;

    @Value("${parameter.car-position.y:0}")
    private int positionY;

    private final CarRepository carRepository;

    @PostConstruct
    private void createInitNumberOfCars() {
        carRepository.saveAll(generate(Car::new)
                .limit(carQuantity)
                .map(car -> car.setIsBooked(false)
                        .setPositionX(positionX)
                        .setPositionY(positionY))
                .toList());
    }
}
