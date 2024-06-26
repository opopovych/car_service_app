package mate.academy.carservice.utility;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.dto.car.RentedCarDto;
import mate.academy.carservice.dto.rental.RentalDto;
import mate.academy.carservice.dto.rental.RentalDtoWithoutCarInfo;
import mate.academy.carservice.model.Rental;
import mate.academy.carservice.model.Role;
import mate.academy.carservice.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class TestRentalProvider {

    public Rental createNewRental(User user, Boolean isActive) {
        return new Rental()
                .setRentalDate(LocalDate.now())
                .setReturnDate(LocalDate.now().plusDays(7))
                .setActualReturnDate(isActive ? null : LocalDate.now())
                .setUser(user);
    }

    public User createCustomerUser() {
        User user = createUser();
        user.getRoles().add(new Role().setRoleName(Role.RoleName.CUSTOMER));
        return user;
    }

    public User createManagerUser() {
        User user = createUser();
        user.getRoles().add(new Role().setRoleName(Role.RoleName.MANAGER));
        return user;
    }

    public User createUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "Password1234$";

        User user = new User();
        user.setId(2L)
                .setEmail("email@example.com")
                .setPassword(passwordEncoder.encode(password))
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setDeleted(false);
        return user;
    }

    public RentalDtoWithoutCarInfo createDtoWithoutCarInfo(Rental rental) {
        return new RentalDtoWithoutCarInfo()
                .setId(rental.getId())
                .setUserId(rental.getUser().getId())
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate())
                .setActualReturnDate(rental.getActualReturnDate());
    }

    public RentalDto createRentalDto(Rental rental) {
        return new RentalDto()
                .setId(1L)
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate())
                .setActualReturnDate(rental.getActualReturnDate())
                .setRentedCarDto(new RentedCarDto().setId(1L))
                .setUserId(rental.getUser().getId());
    }
}
