package ruslan.simakov.taxi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static ruslan.simakov.taxi.util.TestFactory.ONE;
import static ruslan.simakov.taxi.util.TestFactory.ZERO;
import static ruslan.simakov.taxi.util.TestFactory.createCar;


@DataJpaTest
class CarRepositoryTest {

    @Autowired
    CarRepository repository;

    @Test
    public void saveAndFindAllTest() {
        var car = createCar();
        assertThat(car.getCarId()).isNull();

        repository.save(car);

        var cars = repository.findAll();
        assertThat(cars).hasSize(1);
        assertThat(cars).containsOnly(car);

        var carFromDb = repository.findById(ONE);
        assertThat(carFromDb.isPresent()).isTrue();
        assertThat(carFromDb.get().getIsBooked()).isFalse();
        assertThat(carFromDb.get().getWithPassenger()).isFalse();
        assertThat(carFromDb.get().getCarId()).isEqualTo(ONE);
        assertThat(carFromDb.get().getPositionX()).isEqualTo(ZERO);
        assertThat(carFromDb.get().getPositionY()).isEqualTo(ZERO);
        assertThat(carFromDb.get().getSourceX()).isEqualTo(ZERO);
        assertThat(carFromDb.get().getSourceY()).isEqualTo(ZERO);
        assertThat(carFromDb.get().getDestinationX()).isEqualTo(ZERO);
        assertThat(carFromDb.get().getDestinationY()).isEqualTo(ZERO);
    }
}
