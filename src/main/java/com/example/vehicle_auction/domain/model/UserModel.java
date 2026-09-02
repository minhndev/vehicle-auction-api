package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.Gender;
import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel extends FullModel {
    private String firstName;
    private String lastName;
    private String identityNumber;
    private LocalDate birthdate;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private String avatarURL;
    private AccountModel account;

    public String getFullName() {
        String first = this.firstName != null ? this.firstName : "";
        String last = this.lastName != null ? this.lastName : "";
        return (first + " " + last).trim();
    }

    public boolean canParticipateInAuction() {
        if (this.birthdate == null) {
            return false;
        }
        int age = Period.between(this.birthdate, LocalDate.now()).getYears();
        return age >= 18;
    }

    public void updateProfile(String firstName, String lastName, String phoneNumber, String address, String identityNumber, String avatarURL) {
        if (firstName != null && !firstName.isBlank()) this.firstName = firstName;
        if (lastName != null && !lastName.isBlank()) this.lastName = lastName;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (address != null && !address.isBlank()) this.address = address;
        if (identityNumber != null && !identityNumber.isBlank()) this.identityNumber = identityNumber;
        if (avatarURL != null && !avatarURL.isBlank()) this.avatarURL = avatarURL;
    }

}
