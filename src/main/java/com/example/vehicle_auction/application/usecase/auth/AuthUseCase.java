package com.example.vehicle_auction.application.usecase.auth;

import com.example.vehicle_auction.application.dto.auth.AuthResponse;
import com.example.vehicle_auction.application.dto.auth.LoginRequest;
import com.example.vehicle_auction.application.dto.auth.RegisterRequest;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (!req.password().equals(req.confirmPassword()))
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_INVALID);

        if (accountRepository.existsByEmail(req.email()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        AccountModel account = new AccountModel();
        account.setId(UUID.randomUUID());
        account.setEmail(req.email());
        account.setPassword(passwordEncoder.encode(req.password()));
        account.setActive(true);
        account.setVerified(false);

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

//        accountRepository.save(account);
        userRepository.save(user);

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

    private AuthResponse generateAuthResponse(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }
}
