package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.ContactModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactEntityMapper {
    ContactModel toDomain(Contact contact);

    Contact toEntity(ContactModel contact);
}
