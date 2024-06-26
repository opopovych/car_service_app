package mate.academy.carservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Create a new car",
            description = "Create a new car")
    public CarDtoResponse createCar(@RequestBody @Valid CarDtoRequest createCarDto) {
        return carService.createCar(createCarDto);
    }

    @GetMapping
    @Operation(summary = "Get all cars in pages",
            description = "Get all cars in pages")
    public List<CarDtoResponse> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by id",
            description = "Get a car by id")
    Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update car info by id",
            description = "Update car info by id")
    CarDtoResponse updateCarInfo(@PathVariable Long id,
                                 @RequestBody UpdateCarInfoRequestDto requestDto) {
        return carService.updateCarInfo(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a car by id",
            description = "Delete a car by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
