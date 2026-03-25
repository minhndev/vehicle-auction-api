package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.ContactModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ContactRepository {
    ContactModel save(ContactModel contact);

    Page<ContactModel> findAllByDeletedFalse(Pageable pageable);

    Page<ContactModel> findAllByDeletedTrue(Pageable pageable);

    Optional<ContactModel> findById(UUID id);

    Optional<ContactModel> findByPhoneNumber(String phoneNumber);

    Optional<ContactModel> findByEmail(String email);
}
