package com.example.personregistry.repository;

import com.example.personregistry.model.Person;
import com.example.personregistry.model.Address;
import com.example.personregistry.model.AddressType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void whenFindByLastName_thenReturnPersons() {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        entityManager.persist(person);
        entityManager.flush();

        // when
        List<Person> found = personRepository.findByLastNameIgnoreCase("doe");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getLastName()).isEqualTo("Doe");
    }

    @Test
    void whenFindByIdWithAddresses_thenReturnPersonWithAddresses() {
        // given
        Person person = new Person("Jane", "Smith", LocalDate.of(1985, 5, 15));
        entityManager.persist(person);

        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);
        address.setCountry("Hungary");
        address.setCity("Budapest");
        address.setZipCode("1011");
        address.setStreet("Main Street");
        entityManager.persist(address);

        entityManager.flush();

        // when
        Optional<Person> found = personRepository.findByIdWithAddresses(person.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getAddresses()).hasSize(1);
    }

    @Test
    void whenExistsByFirstNameAndLastNameAndBirthDate_thenReturnTrue() {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        entityManager.persist(person);
        entityManager.flush();

        // when
        boolean exists = personRepository.existsByFirstNameAndLastNameAndBirthDate(
                "John", "Doe", LocalDate.of(1990, 1, 1));

        // then
        assertThat(exists).isTrue();
    }
}