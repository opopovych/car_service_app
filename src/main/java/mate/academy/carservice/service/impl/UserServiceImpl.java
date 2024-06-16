package mate.academy.carservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.auth.dto.GetProfileInfoDto;
import mate.academy.carservice.auth.dto.UpdateRoleRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterResponseDto;
import mate.academy.carservice.exception.RegistrationException;
import mate.academy.carservice.mapper.UserMapper;
import mate.academy.carservice.model.Role;
import mate.academy.carservice.model.User;
import mate.academy.carservice.repo.RoleRepository;
import mate.academy.carservice.repo.UserRepository;
import mate.academy.carservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email " + requestDto.getEmail()
                    + " already exists");
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.getRoles().add(roleRepository.findByRoleName(Role.RoleName.CUSTOMER));

        User savedUser = userRepository.save(user);
        return userMapper.entityToRegisterResponseDto(savedUser);
    }

    @Override
    public GetProfileInfoDto getUserInfo(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }

        User user = findUserByEmail(email);
        return userMapper.entityToUserInfoResponseDto(user);
    }

    @Override
    @Transactional
    public GetProfileInfoDto updateUserInfo(String email, UserRegisterRequestDto requestDto) {
        if (email == null || requestDto == null) {
            throw new IllegalArgumentException("Email and request data must not be null");
        }

        User user = findUserByEmail(email);
        userMapper.updateUserInfo(user, requestDto);
        User savedUpdatedUser = userRepository.save(user);
        return userMapper.entityToUserInfoResponseDto(savedUpdatedUser);
    }

    @Override
    @Transactional
    public void updateRole(Long userId, UpdateRoleRequestDto requestDto) {
        if (userId == null || requestDto == null) {
            throw new IllegalArgumentException("User ID and request data must not be null");
        }

        checkManagerId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId
                        + " was not found"));

        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role with id "
                        + requestDto.getRoleId() + " was not found"));

        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        checkManagerId(userId);
        userRepository.deleteById(userId);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email "
                        + email + " was not found"));
    }

    private void checkManagerId(Long userId) {
        if (userId == 1) {
            throw new RuntimeException("Manager with id 1 can not do any updates with yourself");
        }
    }
}
