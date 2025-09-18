package com.example.personregistry.service;

import com.example.personregistry.model.Person;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.MaximumAddressException;

import java.time.LocalDate;
import java.util.List;

public interface PersonService {
    List<Person> getAllPersons();
    Person getPersonById(Long id) throws ResourceNotFoundException;
    Person createPerson(Person person);
    Person updatePerson(Long id, Person personDetails) throws ResourceNotFoundException;
    void deletePerson(Long id) throws ResourceNotFoundException;
    List<Person> searchPersonsByLastName(String lastName);
    Person getPersonWithAddresses(Long id) throws ResourceNotFoundException;
    List<Person> getPersonsWithMultipleAddresses();
    boolean personExists(String firstName, String lastName, LocalDate birthDate);
}