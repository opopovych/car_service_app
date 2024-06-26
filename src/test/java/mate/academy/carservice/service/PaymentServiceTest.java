package mate.academy.carservice.service;

import java.net.MalformedURLException;
import java.util.List;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.repo.PaymentRepository;
import mate.academy.carservice.service.impl.PaymentServiceImpl;
import mate.academy.carservice.utility.TestPaymentProvider;
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
    @InjectMocks
    private TestPaymentProvider testPaymentProvider;

    @Test
    void getPaymentsByUserId_haveOnePayment_returnPaymentListSizeAsOne()
            throws MalformedURLException {
        Long userId = 1L;
        Payment payment = testPaymentProvider.createPaymentTest();
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(List.of(payment));
        List<Payment> actual = paymentService.getPaymentsByUserId(userId);
        Assertions.assertEquals(actual.size(), 1);
    }

    @Test
    void createPaymentSession_oneSession_returnSavedSession() throws MalformedURLException {
        Payment payment = testPaymentProvider.createPaymentTest();
        Mockito.when(paymentRepository.save(payment)).thenReturn(payment);
        Payment actual = paymentService.createPaymentSession(payment);
        Assertions.assertEquals(actual, payment);
    }
}
