package mate.academy.carservice.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.model.payment.Status;
import mate.academy.carservice.repo.PaymentRepository;
import mate.academy.carservice.service.PaymentService;
import mate.academy.carservice.service.notification.NotificationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;

    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Payment createPaymentSession(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void updatePaymentStatus(String sessionId, Status status) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.save(payment);
            notificationService.sendSuccessfulPaymentNotification(payment.getRental().getId());
        }
    }
}
