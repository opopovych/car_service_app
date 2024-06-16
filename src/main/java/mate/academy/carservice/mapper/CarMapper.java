package mate.academy.carservice.mapper;

import mate.academy.carservice.config.MapperConfig;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarDtoResponse entityToCarDto(Car car);

    Car createCarDtoToCar(CarDtoRequest createCarDto);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "brand",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "model",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "inventory",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dailyFee",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Car updateCarInfo(@MappingTarget Car car, UpdateCarInfoRequestDto requestDto);
}
