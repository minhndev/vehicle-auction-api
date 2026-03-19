package com.example.vehicle_auction.application.usecase.auth;

import com.example.vehicle_auction.application.dto.auth.AuthResponse;
import com.example.vehicle_auction.application.dto.auth.LoginRequest;
import com.example.vehicle_auction.application.dto.auth.RegisterRequest;
import com.example.vehicle_auction.application.usecase.mail.SendRegistrationEmailUseCase;
import com.example.vehicle_auction.domain.enums.Gender;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthUseCaseTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private SendRegistrationEmailUseCase sendRegistrationEmailUseCase;

    @InjectMocks
    private AuthUseCase authUseCase;

    void setUp() {
        ReflectionTestUtils.setField(authUseCase, "apiBaseUrl", "http://localhost:8080");
    }

    @Test
    void should_ThrowException_When_RegisterPasswordDoNotMatch() {
        RegisterRequest req = new RegisterRequest(
                "test@test.com", "pass123", "wrongpass", "John", "Doe",
                "123456789", LocalDate.of(1990, 1, 1), Gender.MALE, "1234567890", "Address", "url"
        );

        AppException exception = assertThrows(AppException.class, () -> authUseCase.register(req));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFIRM_PASSWORD_INVALID);
    }

    @Test
    void should_ThrowException_When_RegisterEmailAlreadyExists() {
        RegisterRequest req = new RegisterRequest(
                "test@test.com", "pass123", "pass123", "John", "Doe",
                "123456789", LocalDate.of(1990, 1, 1), Gender.MALE, "1234567890", "Address", "url"
        );

        when(accountRepository.existsByEmail(req.email())).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> authUseCase.register(req));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    void should_SuccessfullyRegister_When_ValidRequest() {
        RegisterRequest req = new RegisterRequest(
                "test@test.com", "pass123", "pass123", "John", "Doe",
                "123456789", LocalDate.of(1990, 1, 1), Gender.MALE, "1234567890", "Address", "url"
        );

        RoleModel userRole = new RoleModel();
        userRole.setName("USER");

        UserDetails mockUserDetails = mock(UserDetails.class);

        when(accountRepository.existsByEmail(req.email())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userDetailsService.loadUserByUsername(req.email())).thenReturn(mockUserDetails);
        when(jwtService.generateAccessToken(mockUserDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(mockUserDetails)).thenReturn("refresh-token");

        AuthResponse res = authUseCase.register(req);

        assertThat(res).isNotNull();
        assertThat(res.accessToken()).isEqualTo("access-token");
        verify(userRepository, times(1)).save(any());
        verify(sendRegistrationEmailUseCase, times(1)).execute(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void should_ThrowException_When_LoginPasswordIsIncorrect() {
        LoginRequest req = new LoginRequest("test@test.com", "wrongpass");
        AccountModel account = new AccountModel();
        account.setPassword("encodedPassword");

        when(accountRepository.findByEmail(req.email())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(req.password(), account.getPassword())).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> authUseCase.login(req));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_UNAUTHORIZED);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void should_SuccessfullyLogin_When_CredentialAreCorrect() {
        LoginRequest req = new LoginRequest("test@test.com", "pass123");
        AccountModel account = new AccountModel();
        account.setEmail("test@test.com");
        account.setPassword("encodedPassword");

        account.setActive(true);
        account.setVerified(true);

        UserDetails mockUserDetails = mock(UserDetails.class);

        when(accountRepository.findByEmail(req.email())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(req.password(), account.getPassword())).thenReturn(true);
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(mockUserDetails);
        when(jwtService.generateAccessToken(mockUserDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(mockUserDetails)).thenReturn("refresh-token");

        AuthResponse res = authUseCase.login(req);

        assertThat(res).isNotNull();
        assertThat(res.accessToken()).isEqualTo("access-token");
    }
}
