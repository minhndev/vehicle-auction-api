package com.example.vehicle_auction.infrastructure.seeder;

import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaAccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaRoleRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final JpaUserRepository jpaUserRepository;
    private final JpaAccountRepository jpaAccountRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Checking system initialization data (Database Seeding)...");

        Role adminRoleEntity = jpaRoleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    log.info("The ADMIN role is not yet created; creating a new one...");
                    Role role = new Role();
                    role.setName("ADMIN");
                    role.setDescription("System administrator with full access");
                    role.setSystem(true);
                    role.setActive(true);
                    return jpaRoleRepository.save(role);
                });

        if (!jpaAccountRepository.existsByEmail(adminEmail)) {
            log.info("No default Admin account. Creating account: {}", adminEmail);

            Account account = new Account();
            account.setEmail(adminEmail);
            account.setPassword(passwordEncoder.encode(adminPassword));
            account.setVerified(true);
            account.setActive(true);
            account.setSystem(true);

            account.setRoles(Set.of(adminRoleEntity));

            User user = new User();
            user.setAccount(account);
            user.setFirstName("System");
            user.setLastName("Admin");
            user.setIdentityNumber("000000000000");
            user.setPhoneNumber("+84000000000");

            jpaUserRepository.save(user);

            log.info("Admin account created successfully!");
        } else {
            log.info("The Admin account already exists. Skip the creation step.");
        }
    }
}