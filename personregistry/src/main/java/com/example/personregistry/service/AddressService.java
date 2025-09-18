package com.example.personregistry.service;

import com.example.personregistry.model.Address;
import com.example.personregistry.model.Person;
import com.example.personregistry.model.AddressType;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.MaximumAddressException;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();
    Address getAddressById(Long id) throws ResourceNotFoundException;
    List<Address> getAddressesByPersonId(Long personId);
    Address createAddress(Address address) throws MaximumAddressException;
    Address updateAddress(Long id, Address addressDetails) throws ResourceNotFoundException;
    void deleteAddress(Long id) throws ResourceNotFoundException;
    Address getAddressWithContacts(Long id) throws ResourceNotFoundException;
    List<Address> getAddressesByPersonIdWithContacts(Long personId);
    boolean addressTypeExistsForPerson(Person person, AddressType addressType);
    int countAddressesByPerson(Person person);
}