package mate.academy.carservice.dto.car;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.carservice.model.CarType;

@Data
@Accessors(chain = true)
public class RentedCarDto {
    private Long id;
    private String brand;
    private String model;
    private CarType type;
    private BigDecimal dailyFee;
}
