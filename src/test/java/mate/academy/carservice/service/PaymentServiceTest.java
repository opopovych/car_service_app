package mate.academy.carservice.service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import mate.academy.carservice.model.Rental;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.model.payment.Status;
import mate.academy.carservice.model.payment.Type;
import mate.academy.carservice.repo.PaymentRepository;
import mate.academy.carservice.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void getPaymentsByUserId_haveOnePayment_returnPaymentListSizeAsOne()
            throws MalformedURLException {
        Long userId = 1L;
        Payment payment = createPaymentTest();
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(List.of(payment));
        List<Payment> actual = paymentService.getPaymentsByUserId(userId);
        Assertions.assertEquals(actual.size(), 1);
    }

    @Test
    void createPaymentSession_oneSession_returnSavedSession() throws MalformedURLException {
        Payment payment = createPaymentTest();
        Mockito.when(paymentRepository.save(payment)).thenReturn(payment);
        Payment actual = paymentService.createPaymentSession(payment);
        Assertions.assertEquals(actual, payment);
    }

    private Payment createPaymentTest() throws MalformedURLException {
        Payment payment = new Payment()
                .setStatus(Status.PENDING)
                .setType(Type.PAYMENT)
                .setRental(new Rental())
                .setSessionUrl(new URL("http://default.url"))
                .setSessionId("sessionId")
                .setAmountToPay(BigDecimal.valueOf(100));
        return payment;
    }
}
