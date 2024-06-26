package mate.academy.carservice.repo;

import java.util.List;
import java.util.Optional;
import mate.academy.carservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Override
    Optional<Rental> findById(Long id);

    List<Rental> findAllByUserId(Long userId);

    Optional<Rental> findRentalByIdAndUserId(Long rentalId, Long userId);
}
