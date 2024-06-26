package mate.academy.carservice.repo;

import java.util.Optional;
import mate.academy.carservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Long> {
    Optional<Car> findById(Long id);
}
