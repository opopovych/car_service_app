package mate.academy.carservice.service;

import mate.academy.carservice.auth.dto.GetProfileInfoDto;
import mate.academy.carservice.auth.dto.UpdateRoleRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterResponseDto;
import mate.academy.carservice.exception.RegistrationException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException;

    @Transactional
    GetProfileInfoDto updateUserInfo(String email,
                                     UserRegisterRequestDto requestDto);

    void updateRole(Long userId, UpdateRoleRequestDto requestDto);

    GetProfileInfoDto getUserInfo(String email);

    void deleteUserById(Long userId);
}
