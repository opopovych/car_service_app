package mate.academy.carservice.utility;

import mate.academy.carservice.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUserProvider {
    private static final String PASSWORD = "Password1234$";
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public User createUser(String email) {
        return new User()
                .setId(2L)
                .setEmail(email)
                .setPassword(PASSWORD_ENCODER.encode(PASSWORD))
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setDeleted(false);
    }
}
