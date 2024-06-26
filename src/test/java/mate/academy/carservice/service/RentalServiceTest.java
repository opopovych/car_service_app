package mate.academy.carservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import mate.academy.carservice.dto.rental.CreateRentalRequestDto;
import mate.academy.carservice.dto.rental.RentalDto;
import mate.academy.carservice.dto.rental.RentalDtoWithoutCarInfo;
import mate.academy.carservice.exception.ClosedRentalException;
import mate.academy.carservice.exception.UnauthorizedAccessException;
import mate.academy.carservice.mapper.RentalMapper;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.model.Rental;
import mate.academy.carservice.model.User;
import mate.academy.carservice.repo.CarRepository;
import mate.academy.carservice.repo.RentalRepository;
import mate.academy.carservice.service.impl.RentalServiceImpl;
import mate.academy.carservice.service.notification.NotificationService;
import mate.academy.carservice.utility.TestRentalProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @Mock
    private NotificationService notificationService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private RentalMapper rentalMapper;
    @InjectMocks
    private RentalServiceImpl rentalServiceImpl;

    @InjectMocks
    private TestRentalProvider testRentalProvider;

    @Test
    @DisplayName("Verify getRentalsByUserId() method works when "
            + "userId is null, isRentalActive is true and managerUser")
    public void getRentalsByUserId_UserIdIsNullAndUserManager_ReturnsFilteredRentals() {
        // given
        Long userId = null;
        Boolean isRentalActive = true;
        User managerUser = testRentalProvider.createManagerUser();

        List<Rental> rentals = List.of(
                testRentalProvider.createNewRental(managerUser, true),
                testRentalProvider.createNewRental(managerUser, false)
        );

        List<RentalDtoWithoutCarInfo> expectedRentals = List.of(
                testRentalProvider.createDtoWithoutCarInfo(rentals.get(0)));

        Mockito.when(rentalRepository.findAll())
                .thenReturn(rentals);
        Mockito.when(rentalMapper.entityRentalDtoWithoutCarInfo(rentals.get(0)))
                .thenReturn(expectedRentals.get(0));

        // when
        List<RentalDtoWithoutCarInfo> actualRentals = rentalServiceImpl
                .getRentalsByUserId(userId, isRentalActive, managerUser);

        // then
        Assertions.assertEquals(expectedRentals, actualRentals);

        Mockito.verify(rentalRepository, times(1))
                .findAll();
        Mockito.verify(rentalMapper, times(1))
                .entityRentalDtoWithoutCarInfo(rentals.get(0));
        Mockito.verifyNoMoreInteractions(rentalRepository, rentalMapper);
    }

    @Test
    @DisplayName("Verify getRentalsByUserId() method works when "
            + "userId is not null, isRentalActive is true and managerUser")
    public void getRentalsByUserId_UserIdIsNotNull_ReturnsFilteredRentals() {
        // given
        Long userId = 2L;
        User user1 = testRentalProvider.createUser();
        user1.setId(1L);
        User user2 = testRentalProvider.createUser();
        user2.setId(userId);

        List<Rental> rentals = Arrays.asList(
                testRentalProvider.createNewRental(user1, true),
                testRentalProvider.createNewRental(user2, true)
        );

        List<RentalDtoWithoutCarInfo> expectedRentals = List.of(
                testRentalProvider.createDtoWithoutCarInfo(rentals.get(1)));

        Mockito.when(rentalRepository.findAllByUserId(userId))
                .thenReturn(List.of(rentals.get(1)));
        Mockito.when(rentalMapper.entityRentalDtoWithoutCarInfo(rentals.get(1)))
                .thenReturn(expectedRentals.get(0));

        // when
        List<RentalDtoWithoutCarInfo> actualRentals = rentalServiceImpl
                .getRentalsByUserId(userId, true, user2);

        // then
        Assertions.assertEquals(expectedRentals, actualRentals);

        Mockito.verify(rentalRepository, times(1))
                .findAllByUserId(userId);
        Mockito.verify(rentalMapper, times(1))
                .entityRentalDtoWithoutCarInfo(rentals.get(1));
        Mockito.verifyNoMoreInteractions(rentalRepository, rentalMapper);
    }

    @Test
    @DisplayName("Verify getRentalById() method works for valid rental id and user customer")
    public void getRentalById_ValidRentalIdAndUserCustomer_ReturnsRentalList() {
        //given
        long rentalId = 1L;

        User customerUser = testRentalProvider.createCustomerUser();
        Rental rental = testRentalProvider.createNewRental(customerUser, true);
        RentalDto expectedRentalDto = new RentalDto()
                .setId(rental.getId())
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate())
                .setActualReturnDate(rental.getActualReturnDate())
                .setUserId(rental.getUser().getId());

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        Mockito.when(rentalMapper.entityToRentalDto(rental)).thenReturn(expectedRentalDto);

        //when
        RentalDto actualRentalDto = rentalServiceImpl.getRentalById(rentalId, customerUser);

        //then
        Assertions.assertEquals(expectedRentalDto, actualRentalDto);

        Mockito.verify(rentalRepository, times(1)).findById(rentalId);
        Mockito.verify(rentalMapper, times(1)).entityToRentalDto(rental);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoMoreInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify getRentalById() method works for valid rental id and user manager")
    public void getRentalById_ValidRentalIdAndUserManager_ReturnRental() {
        //given
        long rentalId = 1L;

        User managerUser = testRentalProvider.createManagerUser();
        Rental rental = testRentalProvider.createNewRental(new User().setId(100L), true);
        RentalDto expectedRentalDto = new RentalDto()
                .setId(rental.getId())
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate())
                .setActualReturnDate(rental.getActualReturnDate())
                .setUserId(rental.getUser().getId());

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        Mockito.when(rentalMapper.entityToRentalDto(rental)).thenReturn(expectedRentalDto);

        //when
        RentalDto actualRentalDto = rentalServiceImpl.getRentalById(rentalId, managerUser);

        //then
        Assertions.assertEquals(expectedRentalDto, actualRentalDto);

        Mockito.verify(rentalRepository, times(1)).findById(rentalId);
        Mockito.verify(rentalMapper, times(1)).entityToRentalDto(rental);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoMoreInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify getRentalById() method throws exception for valid rental id "
            + "and invalid user customer")
    public void getRentalById_ValidRentalIdAndInvalidUserCustomer_ReturnRental() {
        //given
        long rentalId = 1L;

        User customerUser = testRentalProvider.createCustomerUser();
        Rental rental = testRentalProvider.createNewRental(new User().setId(100L), true);

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        //when
        Exception exception = assertThrows(
                UnauthorizedAccessException.class,
                () -> rentalServiceImpl.getRentalById(rentalId, customerUser));

        //then
        String expectedMessage = "You do not have access to specified rental(s)";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(rentalRepository, times(1)).findById(rentalId);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify getRentalById() method throws exception for invalid rental id "
            + "and valid user manager")
    public void getRentalById_InvalidRentalIdAndValidUserManager_ReturnRental() {
        //given
        long rentalId = -1L;

        User managerUser = testRentalProvider.createManagerUser();

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalServiceImpl.getRentalById(rentalId, managerUser));

        //then
        String expectedMessage = "Rental with id " + rentalId + " was not found";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(rentalRepository, times(1)).findById(rentalId);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify createRental() method works")
    public void createRental_ValidCarIdAndInventory_ReturnsRentalDto() {
        //given
        CreateRentalRequestDto createRentalRequestDto = new CreateRentalRequestDto()
                .setCarId(1L)
                .setNumberOfDays(7L);
        User customerUser = testRentalProvider.createCustomerUser();
        Car car = new Car()
                .setId(createRentalRequestDto.getCarId())
                .setInventory(1);
        Rental newRental = testRentalProvider.createNewRental(customerUser, true);
        RentalDto expectedRentalDto = testRentalProvider.createRentalDto(newRental);

        Mockito.when(carRepository.findById(car.getId()))
                .thenReturn(Optional.of(car));
        Mockito.when(carRepository.save(car))
                .thenReturn(car);
        Mockito.when(rentalRepository.save(any(Rental.class)))
                .thenReturn(newRental);
        Mockito.when(rentalMapper.entityToRentalDto(newRental))
                .thenReturn(expectedRentalDto);

        //when
        RentalDto actualRentalDto = rentalServiceImpl
                .createRental(createRentalRequestDto, customerUser);

        //then
        Assertions.assertEquals(expectedRentalDto, actualRentalDto);

        Mockito.verify(carRepository, times(1))
                .findById(car.getId());
        Mockito.verify(carRepository, times(1))
                .save(car);
        Mockito.verify(rentalRepository, times(1))
                .save(any(Rental.class));
        Mockito.verify(rentalMapper, times(1))
                .entityToRentalDto(newRental);
        Mockito.verifyNoMoreInteractions(carRepository);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoMoreInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify createRental() throws exception for invalid car inventory")
    public void createRental_InvalidCarInventory_ReturnsRentalDto() {
        //given
        CreateRentalRequestDto createRentalRequestDto = new CreateRentalRequestDto()
                .setCarId(1L)
                .setNumberOfDays(7L);
        User customerUser = testRentalProvider.createCustomerUser();
        Car car = new Car()
                .setId(createRentalRequestDto.getCarId())
                .setInventory(0);

        Mockito.when(carRepository.findById(createRentalRequestDto.getCarId()))
                .thenReturn(Optional.of(car));

        //when
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalServiceImpl.createRental(createRentalRequestDto, customerUser)
        );

        //then
        String expectedMessage = "Sorry, this car with id "
                + createRentalRequestDto.getCarId() + " is not available now";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(carRepository, times(1))
                .findById(createRentalRequestDto.getCarId());
        Mockito.verifyNoInteractions(rentalRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify createRental() throws exception for invalid car id")
    public void createRental_InvalidCarId_ReturnsRentalDto() {
        //given
        CreateRentalRequestDto createRentalRequestDto = new CreateRentalRequestDto()
                .setCarId(-1L)
                .setNumberOfDays(7L);
        User customerUser = testRentalProvider.createCustomerUser();

        Mockito.when(carRepository.findById(createRentalRequestDto.getCarId()))
                .thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalServiceImpl.createRental(createRentalRequestDto, customerUser)
        );

        //then
        String expectedMessage = "Car with id "
                + createRentalRequestDto.getCarId() + " was not found";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(carRepository, times(1))
                .findById(createRentalRequestDto.getCarId());
        Mockito.verifyNoInteractions(rentalRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify setActualReturnDate() method works")
    public void setActualReturnDate_ValidRentalIdAndUserId_ReturnsRentalDto() {
        // given
        Long rentalId = 1L;
        Long userId = 1L;

        Car car = new Car().setId(1L).setInventory(0);

        Rental activeRental = new Rental()
                .setId(rentalId)
                .setCar(car)
                .setUser(new User().setId(userId))
                .setActualReturnDate(null);
        Rental closedRental = new Rental()
                .setId(rentalId)
                .setCar(car)
                .setUser(new User().setId(userId))
                .setActualReturnDate(LocalDate.now());
        RentalDto rentalDto = testRentalProvider.createRentalDto(closedRental);

        Mockito.when(rentalRepository.findRentalByIdAndUserId(rentalId, userId))
                .thenReturn(Optional.of(activeRental));
        Mockito.when(carRepository.findById(car.getId()))
                .thenReturn(Optional.of(car));
        Mockito.when(rentalRepository.save(any(Rental.class)))
                .thenReturn(closedRental);
        Mockito.when(rentalMapper.entityToRentalDto(any(Rental.class)))
                .thenReturn(rentalDto);

        // when
        RentalDto actualRentalDto = rentalServiceImpl.setActualReturnDate(rentalId, userId);

        // then
        Assertions.assertNotNull(actualRentalDto.getActualReturnDate());

        Mockito.verify(rentalRepository, times(1))
                .findRentalByIdAndUserId(rentalId, userId);
        Mockito.verify(carRepository, times(1))
                .findById(car.getId());
        Mockito.verify(rentalRepository, times(1))
                .save(any(Rental.class));
        Mockito.verify(rentalMapper, times(1))
                .entityToRentalDto(any(Rental.class));
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoMoreInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify setActualReturnDate() method throws exception")
    public void setActualReturnDate_InvalidRentalIdAndUserId_ThrowsException() {
        // given
        Long rentalId = -1L;
        Long userId = -1L;

        Mockito.when(rentalRepository.findRentalByIdAndUserId(rentalId, userId))
                .thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalServiceImpl.setActualReturnDate(rentalId, userId)
        );

        // then
        String expectedMessage = "User with id " + userId
                + " does not have rental with id " + rentalId;
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(rentalRepository, times(1))
                .findRentalByIdAndUserId(rentalId, userId);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoInteractions(carRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }

    @Test
    @DisplayName("Verify setActualReturnDate() method throws exception")
    public void setActualReturnDate_ClosedRental_ThrowsException() {
        // given
        Long rentalId = 1L;
        Long userId = 1L;

        Car car = new Car().setId(1L).setInventory(0);

        Rental closedRental = new Rental()
                .setId(rentalId)
                .setCar(car)
                .setUser(new User().setId(userId))
                .setActualReturnDate(LocalDate.now());

        Mockito.when(rentalRepository.findRentalByIdAndUserId(rentalId, userId))
                .thenReturn(Optional.of(closedRental));

        // when
        Exception exception = assertThrows(
                ClosedRentalException.class,
                () -> rentalServiceImpl.setActualReturnDate(rentalId, userId)
        );

        // then
        String expectedMessage = "Rental with id "
                + closedRental.getId() + " was already closed";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

        Mockito.verify(rentalRepository, times(1))
                .findRentalByIdAndUserId(rentalId, userId);
        Mockito.verifyNoMoreInteractions(rentalRepository);
        Mockito.verifyNoInteractions(carRepository);
        Mockito.verifyNoInteractions(rentalMapper);
    }
}
