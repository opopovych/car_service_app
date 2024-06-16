package mate.academy.carservice.service;

import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.mapper.CarMapper;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.model.CarType;
import mate.academy.carservice.repo.CarRepository;
import mate.academy.carservice.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("Create new car by CarDtoRequest and return CarDtoResponse")
    void createCar_validCarDtoRequest_carDtoResponse() {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();
        Car car = createTestCar(carDtoRequest);
        CarDtoResponse carDtoResponse = createTestCarDtoResponse(car);

        Mockito.when(carMapper.createCarDtoToCar(carDtoRequest)).thenReturn(car);
        Mockito.when(carRepository.save(car)).thenReturn(car);
        Mockito.when(carMapper.entityToCarDto(car)).thenReturn(carDtoResponse);

        CarDtoResponse actual = carService.createCar(carDtoRequest);

        Assertions.assertEquals(actual, carDtoResponse);
    }

    @Test
    @DisplayName("Create one car and check size in list")
    void getAllCars_createOneCar_returnListWithOneCar() {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();
        Car car = createTestCar(carDtoRequest);
        CarDtoResponse carDtoResponse = createTestCarDtoResponse(car);

        Mockito.when(carRepository.findAll()).thenReturn(List.of(car));
        Mockito.when(carMapper.entityToCarDto(car)).thenReturn(carDtoResponse);

        List<CarDtoResponse> cars = carService.getAllCars();

        Assertions.assertEquals(cars.size(), 1);
    }

    @Test
    @DisplayName("Create one car and get it by id")
    void getCarById_createOneCar_returnCarWithIdOne() {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();
        Car car = createTestCar(carDtoRequest);

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Car actual = carService.getCarById(car.getId());
        Assertions.assertEquals(actual, car);
    }

    @Test
    @DisplayName("Create one car and check if two cars are equals")
    void updateCarInfo_createOneCar_returnUpdatedCar() {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();
        Car car = createTestCar(carDtoRequest);
        CarDtoResponse carDtoResponse = createTestCarDtoResponse(car);

        UpdateCarInfoRequestDto updatedRequest = createTestUpdateCar();

        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(carMapper.updateCarInfo(car, updatedRequest)).thenReturn(car);
        Mockito.when(carRepository.save(car)).thenReturn(car);
        Mockito.when(carMapper.entityToCarDto(car)).thenReturn(carDtoResponse);

        CarDtoResponse actual = carService.updateCarInfo(car.getId(), updatedRequest);

        Assertions.assertEquals(actual, carDtoResponse);
    }

    @Test
    @DisplayName("Create one car and delete it, then return empty list")
    void deleteCar_createOneCar_returnEmptyList() {
        long carId = 1L;

        //when
        carService.deleteCar(carId);

        //then
        Mockito.verify(carRepository, times(1)).deleteById(carId);
        Mockito.verifyNoMoreInteractions(carRepository);
        Mockito.verifyNoInteractions(carMapper);
    }

    private CarDtoRequest createTestCarDtoRequest() {
        CarDtoRequest carDtoRequest = new CarDtoRequest()
                .setModel("Freemont")
                .setBrand("Fiat")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(100));
        return carDtoRequest;
    }

    private CarDtoResponse createTestCarDtoResponse(Car car) {
        CarDtoResponse carDtoResponse = new CarDtoResponse()
                .setModel(car.getModel())
                .setBrand(car.getBrand())
                .setType(car.getType());
        return carDtoResponse;
    }

    private Car createTestCar(CarDtoRequest carDtoRequest) {
        Car car = new Car()
                .setModel(carDtoRequest.getModel())
                .setBrand(carDtoRequest.getBrand())
                .setType(carDtoRequest.getType())
                .setInventory(carDtoRequest.getInventory())
                .setDailyFee(carDtoRequest.getDailyFee());
        return car;
    }

    private UpdateCarInfoRequestDto createTestUpdateCar() {
        UpdateCarInfoRequestDto carDtoRequestUpdate = new UpdateCarInfoRequestDto()
                .setModel("Ducato")
                .setBrand("Fiat")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(100));
        return carDtoRequestUpdate;
    }
}
