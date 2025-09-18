package com.example.personregistry.controller;

import com.example.personregistry.model.Person;
import com.example.personregistry.service.PersonService;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.exception.DuplicateResourceException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        try {
            Person person = personService.getPersonById(id);
            return ResponseEntity.ok(person);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        try {
            // Check if person already exists
            if (personService.personExists(person.getFirstName(), person.getLastName(), person.getBirthDate())) {
                throw new DuplicateResourceException("Person", "name and birthdate",
                        person.getFirstName() + " " + person.getLastName() + " (" + person.getBirthDate() + ")");
            }

            Person createdPerson = personService.createPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @Valid @RequestBody Person personDetails) {
        try {
            Person updatedPerson = personService.updatePerson(id, personDetails);
            return ResponseEntity.ok(updatedPerson);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPersonsByLastName(@RequestParam String lastName) {
        List<Person> persons = personService.searchPersonsByLastName(lastName);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}/with-addresses")
    public ResponseEntity<Person> getPersonWithAddresses(@PathVariable Long id) {
        try {
            Person person = personService.getPersonWithAddresses(id);
            return ResponseEntity.ok(person);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/with-multiple-addresses")
    public ResponseEntity<List<Person>> getPersonsWithMultipleAddresses() {
        List<Person> persons = personService.getPersonsWithMultipleAddresses();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkPersonExists(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String birthDate) {

        boolean exists = personService.personExists(firstName, lastName, java.time.LocalDate.parse(birthDate));
        return ResponseEntity.ok(exists);
    }
}