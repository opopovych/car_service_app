package mate.academy.carservice.utility;

import java.math.BigDecimal;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.model.CarType;

public class TestCarProvider {

    public CarDtoRequest createTestCarDtoRequest() {
        CarDtoRequest carDtoRequest = new CarDtoRequest()
                .setModel("Freemont")
                .setBrand("Fiat")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(100));
        return carDtoRequest;
    }

    public CarDtoResponse createTestCarDtoResponse(Car car) {
        CarDtoResponse carDtoResponse = new CarDtoResponse()
                .setModel(car.getModel())
                .setBrand(car.getBrand())
                .setType(car.getType());
        return carDtoResponse;
    }

    public Car createTestCar(CarDtoRequest carDtoRequest) {
        Car car = new Car()
                .setModel(carDtoRequest.getModel())
                .setBrand(carDtoRequest.getBrand())
                .setType(carDtoRequest.getType())
                .setInventory(carDtoRequest.getInventory())
                .setDailyFee(carDtoRequest.getDailyFee());
        return car;
    }

    public UpdateCarInfoRequestDto createTestUpdateCar() {
        UpdateCarInfoRequestDto carDtoRequestUpdate = new UpdateCarInfoRequestDto()
                .setModel("Ducato")
                .setBrand("Fiat")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(100));
        return carDtoRequestUpdate;
    }
}
