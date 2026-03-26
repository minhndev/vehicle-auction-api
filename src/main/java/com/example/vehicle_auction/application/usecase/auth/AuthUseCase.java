package com.example.vehicle_auction.application.usecase.auth;

import com.example.vehicle_auction.application.dto.auth.*;
import com.example.vehicle_auction.application.usecase.mail.SendRegistrationEmailUseCase;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final SendRegistrationEmailUseCase sendRegistrationEmailUseCase;

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.auth.reset-token-expiration-minutes:30}")
    private long resetTokenExpirationMinutes;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (!req.password().equals(req.confirmPassword()))
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_INVALID);

        if (accountRepository.existsByEmail(req.email()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        RoleModel userRoleModel = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        AccountModel account = new AccountModel();
        account.setId(UUID.randomUUID());
        account.setEmail(req.email());
        account.setPassword(passwordEncoder.encode(req.password()));
        account.setActive(true);
        account.setVerified(false);

        String otp = generateOTP();
        account.setVerificationToken(otp);
        account.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(5));

        account.setSystem(false);

        account.setRoles(Set.of(userRoleModel));

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setAccount(account);
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setIdentityNumber(req.identityNumber());
        user.setBirthdate(req.birthdate());
        user.setGender(req.gender());
        user.setPhoneNumber(req.phoneNumber());
        user.setAddress(req.address());
        user.setAvatarURL(req.avatarURL());

        userRepository.save(user);

        String fullName = req.firstName() + " " + req.lastName();
        String subject = "Verify your Vehicle Auction Account";

        String body = "Hello " + fullName + ",\n\n" +
                "Welcome to Vehicle Auction. Your verification code is: " + otp + "\n" +
                "This code is valid for 5 minutes.\n\n" +
                "Happy Bidding!";

        sendRegistrationEmailUseCase.execute(req.email(), fullName, subject, body);

        return generateAuthResponse(account.getEmail());
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        AccountModel account = accountRepository.findByEmail(req.email())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_UNAUTHORIZED));

        if (!passwordEncoder.matches(req.password(), account.getPassword())) {
            account.recordFailedLogin();
            accountRepository.save(account);
            throw new AppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }

        account.recordSuccessfulLogin();
        accountRepository.save(account);

        return generateAuthResponse(account.getEmail());
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken, true);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtService.isTokenValid(refreshToken, userDetails, true)) {
            return generateAuthResponse(email);
        }

        throw new AppException(ErrorCode.REFRESH_UNAUTHORIZED);
    }

    @Transactional
    public void forgotPassword(String email) {
        accountRepository.findByEmail(email).ifPresent(account -> {
            String token = UUID.randomUUID().toString();
            account.setResetPasswordToken(token);
            account.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(resetTokenExpirationMinutes));
            accountRepository.save(account);

            String resetLink = frontendUrl + "/reset-password?token=" + token;
            String subject = "Reset your Vehicle Auction password";
            String body = "We received a password reset request for your account.\n\n" +
                    "Use this link to reset your password:\n" +
                    resetLink + "\n\n" +
                    "If you did not request this, you can ignore this email.";

            sendRegistrationEmailUseCase.execute(account.getEmail(), account.getEmail(), subject, body);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        if (!req.newPassword().equals(req.confirmPassword())) {
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_INVALID);
        }

        AccountModel account = accountRepository.findByResetPasswordToken(req.token())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_INVALID_RESET_TOKEN));

        if (account.getResetPasswordTokenExpiry() == null
                || LocalDateTime.now().isAfter(account.getResetPasswordTokenExpiry())) {
            throw new AppException(ErrorCode.ACCOUNT_RESET_TOKEN_EXPIRED);
        }

        account.setPassword(passwordEncoder.encode(req.newPassword()));
        account.setResetPasswordToken(null);
        account.setResetPasswordTokenExpiry(null);
        accountRepository.save(account);
    }

    @Transactional
    public void verifyAccount(VerifyAccountRequest req) {
        AccountModel account = accountRepository.findByEmail(req.email())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND)); // Bạn cần định nghĩa thêm ErrorCode này nếu chưa có

        if (account.isVerified()) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }

        if (account.getVerificationToken() == null || !account.getVerificationToken().equals(req.otp())) {
            throw new AppException(ErrorCode.ACCOUNT_INVALID_VERIFICATION_TOKEN);
        }

        if (account.getVerificationTokenExpiry() != null && LocalDateTime.now().isAfter(account.getVerificationTokenExpiry())) {
            throw new AppException(ErrorCode.ACCOUNT_VERIFICATION_TOKEN_EXPIRED); // Định nghĩa thêm ErrorCode này
        }

        account.setVerified(true);
        account.setVerificationToken(null);
        account.setVerificationTokenExpiry(null);

        accountRepository.save(account);
    }

    private AuthResponse generateAuthResponse(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
