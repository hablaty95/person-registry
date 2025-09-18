package com.example.personregistry.service;

import com.example.personregistry.model.Contact;
import com.example.personregistry.model.Address;
import com.example.personregistry.model.ContactType;
import com.example.personregistry.exception.ResourceNotFoundException;

import java.util.List;

public interface ContactService {
    List<Contact> getAllContacts();
    Contact getContactById(Long id) throws ResourceNotFoundException;
    List<Contact> getContactsByAddressId(Long addressId);
    Contact createContact(Contact contact);
    Contact updateContact(Long id, Contact contactDetails) throws ResourceNotFoundException;
    void deleteContact(Long id) throws ResourceNotFoundException;
    List<Contact> getContactsByType(ContactType contactType);
    boolean contactExists(Address address, ContactType contactType, String contactValue);
}