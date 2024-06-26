package mate.academy.carservice.dto.car;

import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.carservice.model.CarType;

@Data
@Accessors(chain = true)
public class CarDtoResponse {
    private String model;
    private String brand;
    private CarType type;
}
