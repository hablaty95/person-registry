package com.example.personregistry.service.impl;

import com.example.personregistry.model.Address;
import com.example.personregistry.model.Person;
import com.example.personregistry.model.AddressType;
import com.example.personregistry.repository.AddressRepository;
import com.example.personregistry.service.AddressService;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.MaximumAddressException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private static final int MAX_ADDRESSES_PER_PERSON = 2;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Long id) throws ResourceNotFoundException {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAddressesByPersonId(Long personId) {
        return addressRepository.findByPersonId(personId);
    }

    @Override
    public Address createAddress(Address address) throws MaximumAddressException {
        Person person = address.getPerson();
        int addressCount = addressRepository.countByPerson(person);

        if (addressCount >= MAX_ADDRESSES_PER_PERSON) {
            throw new MaximumAddressException(
                    "Person can have maximum " + MAX_ADDRESSES_PER_PERSON + " addresses. " +
                            "Current count: " + addressCount
            );
        }

        // Check if address type already exists for this person
        if (addressRepository.existsByPersonAndAddressType(person, address.getAddressType())) {
            throw new MaximumAddressException(
                    "Address type '" + address.getAddressType() + "' already exists for this person"
            );
        }

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address addressDetails) throws ResourceNotFoundException {
        Address address = getAddressById(id);

        address.setAddressType(addressDetails.getAddressType());
        address.setCountry(addressDetails.getCountry());
        address.setCity(addressDetails.getCity());
        address.setZipCode(addressDetails.getZipCode());
        address.setStreet(addressDetails.getStreet());
        address.setHouseNumber(addressDetails.getHouseNumber());

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) throws ResourceNotFoundException {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressWithContacts(Long id) throws ResourceNotFoundException {
        return addressRepository.findByIdWithContacts(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAddressesByPersonIdWithContacts(Long personId) {
        return addressRepository.findByPersonIdWithContacts(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean addressTypeExistsForPerson(Person person, AddressType addressType) {
        return addressRepository.existsByPersonAndAddressType(person, addressType);
    }

    @Override
    @Transactional(readOnly = true)
    public int countAddressesByPerson(Person person) {
        return addressRepository.countByPerson(person);
    }
}