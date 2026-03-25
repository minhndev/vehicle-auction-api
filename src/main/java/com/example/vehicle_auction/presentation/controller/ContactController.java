package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.contact.ContactRequest;
import com.example.vehicle_auction.application.dto.contact.ContactResponse;
import com.example.vehicle_auction.application.usecase.contact.CreateContactUseCase;
import com.example.vehicle_auction.application.usecase.contact.GetContactUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Tag(name = "Contact API", description = "APIs for managing user contact and support requests")
public class ContactController {
    private final CreateContactUseCase createContactUseCase;
    private final GetContactUseCase getContactUseCase;

    @Operation(summary = "Submit a new contact request", description = "Create a new support or contact request from a user")
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@RequestBody @Valid ContactRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createContactUseCase.execute(req));
    }

    @Operation(summary = "Get contact details by ID", description = "Retrieve details of a specific contact request using its UUID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CONTACT_VIEW_DETAILS')")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getContactUseCase.getContactById(id));
    }

    @Operation(summary = "Get a list of active contacts", description = "Retrieve a paginated list of all active (non-deleted) contact requests")
    @GetMapping
    @PreAuthorize("hasAuthority('CONTACT_VIEW')")
    public ResponseEntity<Page<ContactResponse>> getAllActiveContact(@ParameterObject Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getContactUseCase.getAllActiveContacts(pageable));
    }
}
