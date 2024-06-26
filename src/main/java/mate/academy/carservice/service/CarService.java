package mate.academy.carservice.service;

import java.util.List;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.model.Car;

public interface CarService {
    CarDtoResponse createCar(CarDtoRequest carDto);

    List<CarDtoResponse> getAllCars();

    Car getCarById(Long id);

    CarDtoResponse updateCarInfo(Long id, UpdateCarInfoRequestDto requestDto);

    void deleteCar(Long id);
}
