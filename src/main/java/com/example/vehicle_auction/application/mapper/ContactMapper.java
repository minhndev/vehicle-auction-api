package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.contact.ContactRequest;
import com.example.vehicle_auction.application.dto.contact.ContactResponse;
import com.example.vehicle_auction.domain.model.ContactModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactModel toDomain(ContactRequest req);
    ContactResponse toResponse(ContactModel contactModel);
}
