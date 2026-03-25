package com.example.vehicle_auction.application.usecase.user;

import com.example.vehicle_auction.application.dto.user.CreateUserRequest;
import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.dto.user.UserResponse;
import com.example.vehicle_auction.application.mapper.UserMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserManagementResponse execute(CreateUserRequest req) {
        if (!req.password().equals(req.confirmPassword()))
            throw new AppException(ErrorCode.ACCOUNT_CONFIRM_PASSWORD_INVALID);

        if (accountRepository.existsByEmail(req.email()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        Set<RoleModel> roles = req.roleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet());

        AccountModel account = new AccountModel();
        account.setId(UUID.randomUUID());
        account.setEmail(req.email());
        account.setPassword(passwordEncoder.encode(req.password()));
        account.setActive(true);
        account.setVerified(false);
        account.setVerificationToken(UUID.randomUUID().toString());
        account.setCreatedAt(LocalDateTime.now());
        account.setRoles(roles);

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setIdentityNumber(req.identityNumber());
        user.setBirthdate(req.birthdate());
        user.setGender(req.gender());
        user.setPhoneNumber(req.phoneNumber());
        user.setAddress(req.address());
        user.setAvatarURL(req.avatarURL());
        user.setCreatedAt(LocalDateTime.now());
        user.setAccount(account);

        UserModel savedUser = userRepository.save(user);

        return userMapper.toManagementResponse(savedUser);
    }
}
