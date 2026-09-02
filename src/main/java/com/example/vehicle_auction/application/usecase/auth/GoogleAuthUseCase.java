package com.example.vehicle_auction.application.usecase.auth;

import com.example.vehicle_auction.application.dto.auth.AuthResponse;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import com.example.vehicle_auction.infrastructure.security.google.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthUseCase {
    private final GoogleTokenVerifier googleTokenVerifier;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public AuthResponse authenticate(String idTokenString) {
        // Verify token from Google
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(idTokenString);

        // Get user info
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String pictureUrl = (String) payload.get("picture");

        // Find or create account & user
        AccountModel account = accountRepository.findByEmail(email).orElseGet(() -> {
            AccountModel newAccount = new AccountModel();
            newAccount.setId(UUID.randomUUID());
            newAccount.setEmail(email);
            newAccount.setPassword("");
            newAccount.setVerified(true);
            newAccount.setActive(true);

            RoleModel userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            newAccount.setRoles(Set.of(userRole));

            UserModel newUser = new UserModel();
            newUser.setId(UUID.randomUUID());
            newUser.setAccount(newAccount);
            newUser.setFirstName(firstName != null ? firstName : "");
            newUser.setLastName(lastName != null ? lastName : "");
            newUser.setAvatarURL(pictureUrl);

            userRepository.save(newUser);
            return newAccount;
        });

        // Generate JWT Token
        CustomUserDetails userDetails = new CustomUserDetails(account);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }
}
