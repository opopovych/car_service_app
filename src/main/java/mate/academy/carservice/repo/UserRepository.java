package mate.academy.carservice.repo;

import java.util.Optional;
import mate.academy.carservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User u "
            + "WHERE u.email = :email ")
    Optional<User> findByEmail(String email);
}
