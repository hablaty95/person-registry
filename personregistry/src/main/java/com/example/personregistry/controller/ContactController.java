package com.example.personregistry.controller;

import com.example.personregistry.model.Contact;
import com.example.personregistry.service.ContactService;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.DuplicateResourceException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        try {
            Contact contact = contactService.getContactById(id);
            return ResponseEntity.ok(contact);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<List<Contact>> getContactsByAddressId(@PathVariable Long addressId) {
        List<Contact> contacts = contactService.getContactsByAddressId(addressId);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/type/{contactType}")
    public ResponseEntity<List<Contact>> getContactsByType(@PathVariable String contactType) {
        List<Contact> contacts = contactService.getContactsByType(
                com.example.personregistry.model.ContactType.valueOf(contactType.toUpperCase())
        );
        return ResponseEntity.ok(contacts);
    }

    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody Contact contact) {
        try {
            Contact createdContact = contactService.createContact(contact);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @Valid @RequestBody Contact contactDetails) {
        try {
            Contact updatedContact = contactService.updateContact(id, contactDetails);
            return ResponseEntity.ok(updatedContact);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check-exists")
    public ResponseEntity<Boolean> checkContactExists(
            @RequestParam Long addressId,
            @RequestParam String contactType,
            @RequestParam String contactValue) {

        // This would require a new method in service to get address by ID first
        // For simplicity, we'll return a placeholder response
        return ResponseEntity.ok(false);
    }
}