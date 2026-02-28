package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

public class UserModel {
    private UUID id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private LocalDate birthdate;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private String avatarURL;
    private AccountModel account;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean deleted;
    private LocalDateTime deletedAt;

    public UserModel() {
    }

    public UserModel(UUID id, String firstName, String lastName, String identityNumber, LocalDate birthdate, Gender gender, String phoneNumber, String address, String avatarURL, AccountModel account, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, boolean deleted, LocalDateTime deletedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityNumber = identityNumber;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.avatarURL = avatarURL;
        this.account = account;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

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

    public void updateProfile(String firstName, String lastName, String phoneNumber, String address) {
        if (firstName != null && !firstName.isBlank()) this.firstName = firstName;
        if (lastName != null && !lastName.isBlank()) this.lastName = lastName;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (address != null && !address.isBlank()) this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
