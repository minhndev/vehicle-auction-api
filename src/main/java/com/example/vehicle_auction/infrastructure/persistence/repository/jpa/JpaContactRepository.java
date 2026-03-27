package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaContactRepository extends JpaRepository<Contact, UUID> {
    Page<Contact> findAllByDeletedFalse(Pageable pageable);

    Page<Contact> findAllByDeletedTrue(Pageable pageable);

    Optional<Contact> findByPhoneNumber(String phoneNumber);

    Optional<Contact> findByEmail(String email);
}
