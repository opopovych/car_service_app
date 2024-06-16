package mate.academy.carservice.auth.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mate.academy.carservice.model.Role;

@Getter
@Setter
@Accessors(chain = true)
public class GetProfileInfoDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles = new HashSet<>();
}
