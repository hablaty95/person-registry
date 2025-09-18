package com.example.personregistry.service.impl;

import com.example.personregistry.model.Person;
import com.example.personregistry.repository.PersonRepository;
import com.example.personregistry.service.PersonService;
import com.example.personregistry.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPersonById(Long id) throws ResourceNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person updatePerson(Long id, Person personDetails) throws ResourceNotFoundException {
        Person person = getPersonById(id);

        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setBirthDate(personDetails.getBirthDate());

        return personRepository.save(person);
    }

    @Override
    public void deletePerson(Long id) throws ResourceNotFoundException {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> searchPersonsByLastName(String lastName) {
        return personRepository.findByLastNameIgnoreCase(lastName);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPersonWithAddresses(Long id) throws ResourceNotFoundException {
        return personRepository.findByIdWithAddresses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getPersonsWithMultipleAddresses() {
        return personRepository.findPersonsWithMultipleAddresses();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean personExists(String firstName, String lastName, LocalDate birthDate) {
        return personRepository.existsByFirstNameAndLastNameAndBirthDate(firstName, lastName, birthDate);
    }
}