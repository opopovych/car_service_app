package mate.academy.carservice.dto.car;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.carservice.model.CarType;

@Data
@NotNull
@Accessors(chain = true)
public class CarDtoRequest {
    private String model;
    private String brand;
    private CarType type;
    private int inventory;
    private BigDecimal dailyFee;
}
