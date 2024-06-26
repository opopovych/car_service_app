package mate.academy.carservice.utility;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import mate.academy.carservice.model.Rental;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.model.payment.Status;
import mate.academy.carservice.model.payment.Type;
import org.springframework.stereotype.Component;

@Component
public class TestPaymentProvider {
    public Payment createPaymentTest() throws MalformedURLException {
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
