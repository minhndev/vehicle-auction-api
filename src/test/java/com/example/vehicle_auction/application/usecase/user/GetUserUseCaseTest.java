package com.example.vehicle_auction.application.usecase.user;

import com.example.vehicle_auction.application.dto.user.UpdateUserStatusRequest;
import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.mapper.UserMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private GetUserUseCase getUserUseCase;

    @Test
    void should_ReturnPagedManagementUsers_When_GetAllUsers() {
        PageRequest pageable = PageRequest.of(0, 10);
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());

        UserManagementResponse mapped = new UserManagementResponse(
                user.getId(), "John Doe", "John", "Doe", "john@example.com", "0901234567",
                true, true, false, "SYSTEM", null, null, List.of("USER")
        );

        when(userRepository.findAll(pageable, "john", true, true, false))
                .thenReturn(new PageImpl<>(List.of(user), pageable, 1));
        when(userMapper.toManagementResponse(user)).thenReturn(mapped);

        Page<UserManagementResponse> result = getUserUseCase.getAllUsers(pageable, "john", true, true, false);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("john@example.com");
    }

    @Test
    void should_ThrowException_When_UserByIdNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> getUserUseCase.getUserById(userId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void should_UpdateActiveStatus_When_RequestIsValid() {
        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setId(userId);

        AccountModel account = new AccountModel();
        account.setActive(true);
        user.setAccount(account);

        UpdateUserStatusRequest request = new UpdateUserStatusRequest(false);

        UserManagementResponse mapped = new UserManagementResponse(
                userId, "John Doe", "John", "Doe", "john@example.com", "0901234567",
                false, true, false, "SYSTEM", null, null, List.of("USER")
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);
        when(userMapper.toManagementResponse(user)).thenReturn(mapped);

        UserManagementResponse result = getUserUseCase.updateStatus(userId, request);

        assertThat(user.getAccount().isActive()).isFalse();
        assertThat(result.active()).isFalse();
        verify(userRepository).save(eq(user));
    }

    @Test
    void should_GrantSellerRole_When_UserExists() {
        UUID userId = UUID.randomUUID();

        RoleModel sellerRole = new RoleModel();
        sellerRole.setName("SELLER");

        AccountModel account = new AccountModel();
        account.setRoles(new HashSet<>(List.of()));

        UserModel user = new UserModel();
        user.setId(userId);
        user.setAccount(account);

        UserManagementResponse mapped = new UserManagementResponse(
                userId, "John Doe", "John", "Doe", "john@example.com", "0901234567",
                true, true, false, "SYSTEM", null, null, List.of("SELLER")
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("SELLER")).thenReturn(Optional.of(sellerRole));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(account);
        when(userMapper.toManagementResponse(user)).thenReturn(mapped);

        UserManagementResponse result = getUserUseCase.grantSellerRole(userId);

        assertThat(result.roles()).contains("SELLER");
        verify(accountRepository).save(eq(account));
    }
}
