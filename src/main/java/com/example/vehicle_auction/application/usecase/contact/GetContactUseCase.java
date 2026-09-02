package com.example.vehicle_auction.application.usecase.contact;

import com.example.vehicle_auction.application.dto.contact.ContactResponse;
import com.example.vehicle_auction.application.mapper.ContactMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetContactUseCase {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactResponse getContactById(UUID id) {
        return contactRepository.findById(id)
                .map(contactMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CONTACT_NOT_FOUND));
    }

    public Page<ContactResponse> getAllActiveContacts(Pageable pageable) {
        return contactRepository.findAllByDeletedFalse(pageable)
                .map(contactMapper::toResponse);
    }
}
