package mate.academy.carservice.dto.payment;

import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentRequest {
    private Long rentalId;
    private URL sessionUrl;
    private BigDecimal amountToPay;
}
