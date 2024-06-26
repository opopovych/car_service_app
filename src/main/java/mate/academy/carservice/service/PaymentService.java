package mate.academy.carservice.service;

import java.util.List;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.model.payment.Status;

public interface PaymentService {
    Payment createPaymentSession(Payment payment);

    List<Payment> getPaymentsByUserId(Long userId);

    void updatePaymentStatus(String sessionId, Status status);
}
