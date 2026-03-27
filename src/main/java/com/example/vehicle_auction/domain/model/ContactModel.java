package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.ContactStatus;
import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactModel extends FullModel {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String subject;
    private String content;
    private ContactStatus status;
}
