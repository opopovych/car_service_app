package mate.academy.carservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.mapper.CarMapper;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.repo.CarRepository;
import mate.academy.carservice.service.CarService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDtoResponse createCar(CarDtoRequest carDto) {
        Car car = carMapper.createCarDtoToCar(carDto);
        Car savedCar = carRepository.save(car);
        return carMapper.entityToCarDto(savedCar);
    }

    @Override
    public List<CarDtoResponse> getAllCars() {
        List<Car> cars = carRepository.findAll();
        List<CarDtoResponse> carDtos = cars.stream()
                .map(carMapper::entityToCarDto)
                .toList();
        return carDtos;
    }

    @Override
    public Car getCarById(Long id) {
        return findById(id);
    }

    @Override
    public CarDtoResponse updateCarInfo(Long id, UpdateCarInfoRequestDto requestDto) {
        Car car = findById(id);
        Car updatedCar = carMapper.updateCarInfo(car,requestDto);
        Car savedCar = carRepository.save(updatedCar);
        return carMapper.entityToCarDto(savedCar);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    private Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find car"));
    }
}
