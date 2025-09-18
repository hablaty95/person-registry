package com.example.personregistry.service.impl;

import com.example.personregistry.model.Contact;
import com.example.personregistry.model.Address;
import com.example.personregistry.model.ContactType;
import com.example.personregistry.repository.ContactRepository;
import com.example.personregistry.service.ContactService;
import com.example.personregistry.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contact getContactById(Long id) throws ResourceNotFoundException {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> getContactsByAddressId(Long addressId) {
        return contactRepository.findByAddressId(addressId);
    }

    @Override
    public Contact createContact(Contact contact) {
        // Check if similar contact already exists
        Address address = contact.getAddress();
        ContactType contactType = contact.getContactType();
        String contactValue = contact.getContactValue();

        if (contactRepository.existsByAddressAndContactTypeAndContactValue(address, contactType, contactValue)) {
            throw new IllegalArgumentException(
                    "Contact with type '" + contactType + "' and value '" + contactValue +
                            "' already exists for this address"
            );
        }

        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Long id, Contact contactDetails) throws ResourceNotFoundException {
        Contact contact = getContactById(id);

        contact.setContactType(contactDetails.getContactType());
        contact.setContactValue(contactDetails.getContactValue());

        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id) throws ResourceNotFoundException {
        Contact contact = getContactById(id);
        contactRepository.delete(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> getContactsByType(ContactType contactType) {
        return contactRepository.findByContactType(contactType);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean contactExists(Address address, ContactType contactType, String contactValue) {
        return contactRepository.existsByAddressAndContactTypeAndContactValue(address, contactType, contactValue);
    }
}