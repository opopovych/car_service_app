package mate.academy.carservice.service;

import java.util.List;
import mate.academy.carservice.dto.rental.CreateRentalRequestDto;
import mate.academy.carservice.dto.rental.RentalDto;
import mate.academy.carservice.dto.rental.RentalDtoWithoutCarInfo;
import mate.academy.carservice.model.User;

public interface RentalService {
    List<RentalDtoWithoutCarInfo> getRentalsByUserId(Long userId,
                                                     Boolean isRentalActive,
                                                     User user);

    RentalDto getRentalById(Long rentalId, User user);

    RentalDto createRental(CreateRentalRequestDto requestDto, User user);

    RentalDto setActualReturnDate(Long rentalId, Long userId);
}
