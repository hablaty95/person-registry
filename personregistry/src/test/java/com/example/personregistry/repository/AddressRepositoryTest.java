package com.example.personregistry.repository;

import com.example.personregistry.model.Person;
import com.example.personregistry.model.Address;
import com.example.personregistry.model.AddressType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Profile("test")
class AddressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void whenFindByPersonId_thenReturnAddresses() {
        // given
        Person person = new Person("John", "Doe", null);
        entityManager.persist(person);

        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);
        address.setCountry("Hungary");
        address.setCity("Budapest");
        entityManager.persist(address);

        entityManager.flush();

        // when
        List<Address> found = addressRepository.findByPersonId(person.getId());

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCity()).isEqualTo("Budapest");
    }

    @Test
    void whenExistsByPersonAndAddressType_thenReturnTrue() {
        // given
        Person person = new Person("John", "Doe", null);
        entityManager.persist(person);

        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);
        address.setCountry("Hungary");
        address.setCity("Budapest");
        entityManager.persist(address);

        entityManager.flush();

        // when
        boolean exists = addressRepository.existsByPersonAndAddressType(person, AddressType.PERMANENT);

        // then
        assertThat(exists).isTrue();
    }
}