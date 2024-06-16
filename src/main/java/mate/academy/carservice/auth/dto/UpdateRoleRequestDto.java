package mate.academy.carservice.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateRoleRequestDto {
    @NotNull
    @Positive
    private Long roleId;
}
