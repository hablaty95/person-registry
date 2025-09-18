package com.example.personregistry.service;

import com.example.personregistry.model.Person;
import com.example.personregistry.repository.PersonRepository;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    void whenGetAllPersons_thenReturnAllPersons() {
        // given
        Person person1 = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        Person person2 = new Person("Jane", "Smith", LocalDate.of(1985, 5, 15));
        List<Person> persons = Arrays.asList(person1, person2);

        when(personRepository.findAll()).thenReturn(persons);

        // when
        List<Person> result = personService.getAllPersons();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(person1, person2);
    }

    @Test
    void whenGetPersonById_thenReturnPerson() {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // when
        Person result = personService.getPersonById(1L);

        // then
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void whenGetPersonByIdNotFound_thenThrowException() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> personService.getPersonById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Person not found with id: 1");
    }

    @Test
    void whenCreatePerson_thenReturnSavedPerson() {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // when
        Person result = personService.createPerson(person);

        // then
        assertThat(result).isEqualTo(person);
        verify(personRepository).save(person);
    }

    @Test
    void whenDeletePerson_thenPersonShouldBeDeleted() {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        doNothing().when(personRepository).delete(person);

        // when
        personService.deletePerson(1L);

        // then
        verify(personRepository).delete(person);
    }
}