package com.example.personregistry.controller;

import com.example.personregistry.model.Address;
import com.example.personregistry.service.AddressService;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.MaximumAddressException;
import com.example.personregistry.exception.DuplicateResourceException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        try {
            Address address = addressService.getAddressById(id);
            return ResponseEntity.ok(address);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Address>> getAddressesByPersonId(@PathVariable Long personId) {
        List<Address> addresses = addressService.getAddressesByPersonId(personId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<?> createAddress(@Valid @RequestBody Address address) {
        try {
            Address createdAddress = addressService.createAddress(address);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
        } catch (MaximumAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @Valid @RequestBody Address addressDetails) {
        try {
            Address updatedAddress = addressService.updateAddress(id, addressDetails);
            return ResponseEntity.ok(updatedAddress);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/with-contacts")
    public ResponseEntity<Address> getAddressWithContacts(@PathVariable Long id) {
        try {
            Address address = addressService.getAddressWithContacts(id);
            return ResponseEntity.ok(address);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/person/{personId}/with-contacts")
    public ResponseEntity<List<Address>> getAddressesByPersonIdWithContacts(@PathVariable Long personId) {
        List<Address> addresses = addressService.getAddressesByPersonIdWithContacts(personId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/check-address-type")
    public ResponseEntity<Boolean> checkAddressTypeExists(
            @RequestParam Long personId,
            @RequestParam String addressType) {

        // This would require a new method in service to get person by ID first
        // For simplicity, we'll return a placeholder response
        return ResponseEntity.ok(false);
    }

    @GetMapping("/person/{personId}/count")
    public ResponseEntity<Integer> getAddressCountByPerson(@PathVariable Long personId) {
        // This would require a new method in service
        // For simplicity, we'll return a placeholder response
        return ResponseEntity.ok(0);
    }
}