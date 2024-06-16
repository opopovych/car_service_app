package mate.academy.carservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.auth.dto.GetProfileInfoDto;
import mate.academy.carservice.auth.dto.UpdateRoleRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterRequestDto;
import mate.academy.carservice.model.User;
import mate.academy.carservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Tag(name = "User management", description = "Endpoints for managing users")
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "User's information",
            description = "Endpoint for getting information about user")
    public GetProfileInfoDto getUserInfo(Authentication authentication) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        User user = (User) userDetails;
        return userService.getUserInfo(user.getEmail());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Updating user's information",
            description = "Endpoint for updating user's information ")
    public GetProfileInfoDto updateUserInfo(Authentication authentication,
                                            @RequestBody UserRegisterRequestDto requestDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        User user = (User) userDetails;
        return userService.updateUserInfo(user.getEmail(), requestDto);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Updating user's role",
            description = "Endpoint for updating user's role")
    public void updateUserRole(@PathVariable Long id,
                               @RequestBody @Valid UpdateRoleRequestDto requestDto) {
        userService.updateRole(id, requestDto);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Deleting an user by id",
            description = "Endpoint for deleting an user by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
