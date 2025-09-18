package com.example.personregistry.repository;

import com.example.personregistry.model.Address;
import com.example.personregistry.model.Contact;
import com.example.personregistry.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByAddress(Address address);

    List<Contact> findByAddressId(Long addressId);

    List<Contact> findByContactType(ContactType contactType);

    boolean existsByAddressAndContactTypeAndContactValue(Address address, ContactType contactType, String contactValue);
}