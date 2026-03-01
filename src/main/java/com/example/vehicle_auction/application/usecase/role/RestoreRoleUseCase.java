package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestoreRoleUseCase {
    private final RoleRepository roleRepository;

    public void execute(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        role.restore();
        roleRepository.save(role);
    }
}
