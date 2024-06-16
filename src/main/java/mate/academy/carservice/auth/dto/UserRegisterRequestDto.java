package mate.academy.carservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class UserRegisterRequestDto {
    @NotBlank
    @Length(min = 5, max = 50)
    @Email
    private String email;
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Length(min = 8)
    private String password;
}
