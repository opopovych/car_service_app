package mate.academy.carservice.mapper;

import mate.academy.carservice.auth.dto.GetProfileInfoDto;
import mate.academy.carservice.auth.dto.UserRegisterRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterResponseDto;
import mate.academy.carservice.config.MapperConfig;
import mate.academy.carservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegisterResponseDto entityToRegisterResponseDto(User savedUser);

    GetProfileInfoDto entityToUserInfoResponseDto(User user);

    @Mapping(target = "firstName",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastName",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserInfo(@MappingTarget User user, UserRegisterRequestDto requestDto);
}
