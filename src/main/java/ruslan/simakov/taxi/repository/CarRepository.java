package ruslan.simakov.taxi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ruslan.simakov.taxi.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Integer> {
}
