package mate.academy.carservice.repo;

import java.util.List;
import mate.academy.carservice.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN FETCH p.rental r JOIN FETCH r.user u WHERE u.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);

    Payment findBySessionId(String sessionId);
}
