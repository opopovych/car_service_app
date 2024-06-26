package mate.academy.carservice.mapper;

import mate.academy.carservice.config.MapperConfig;
import mate.academy.carservice.dto.rental.RentalDto;
import mate.academy.carservice.dto.rental.RentalDtoWithoutCarInfo;
import mate.academy.carservice.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CarMapper.class)
public interface RentalMapper {
    @Mapping(target = "rentedCarDto", source = "car")
    @Mapping(target = "userId", source = "user.id")
    RentalDto entityToRentalDto(Rental rental);

    @Mapping(target = "userId", source = "user.id")
    RentalDtoWithoutCarInfo entityRentalDtoWithoutCarInfo(Rental rental);
}
