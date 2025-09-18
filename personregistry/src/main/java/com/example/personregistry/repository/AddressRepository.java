package com.example.personregistry.repository;

import com.example.personregistry.model.Address;
import com.example.personregistry.model.AddressType;
import com.example.personregistry.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByPerson(Person person);

    List<Address> findByPersonId(Long personId);

    Optional<Address> findByPersonAndAddressType(Person person, AddressType addressType);

    boolean existsByPersonAndAddressType(Person person, AddressType addressType);

    int countByPerson(Person person);

    @Query("SELECT a FROM Address a LEFT JOIN FETCH a.contacts WHERE a.id = :id")
    Optional<Address> findByIdWithContacts(@Param("id") Long id);

    @Query("SELECT a FROM Address a LEFT JOIN FETCH a.contacts WHERE a.person.id = :personId")
    List<Address> findByPersonIdWithContacts(@Param("personId") Long personId);
}