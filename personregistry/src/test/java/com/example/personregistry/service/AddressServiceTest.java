package com.example.personregistry.service;

import com.example.personregistry.model.Person;
import com.example.personregistry.model.Address;
import com.example.personregistry.model.AddressType;
import com.example.personregistry.repository.AddressRepository;
import com.example.personregistry.exception.MaximumAddressException;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.example.personregistry.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void whenCreateAddressWithAvailableSlot_thenReturnSavedAddress() {
        // given
        Person person = new Person("John", "Doe", null);
        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);

        when(addressRepository.countByPerson(person)).thenReturn(1);
        when(addressRepository.existsByPersonAndAddressType(person, AddressType.PERMANENT)).thenReturn(false);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // when
        Address result = addressService.createAddress(address);

        // then
        assertThat(result).isEqualTo(address);
        verify(addressRepository).save(address);
    }

    @Test
    void whenCreateAddressWithMaxAddresses_thenThrowException() {
        // given
        Person person = new Person("John", "Doe", null);
        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);

        when(addressRepository.countByPerson(person)).thenReturn(2);

        // when & then
        assertThatThrownBy(() -> addressService.createAddress(address))
                .isInstanceOf(MaximumAddressException.class)
                .hasMessageContaining("maximum 2 addresses");
    }

    @Test
    void whenCreateAddressWithDuplicateType_thenThrowException() {
        // given
        Person person = new Person("John", "Doe", null);
        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);

        when(addressRepository.countByPerson(person)).thenReturn(1);
        when(addressRepository.existsByPersonAndAddressType(person, AddressType.PERMANENT)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> addressService.createAddress(address))
                .isInstanceOf(MaximumAddressException.class)
                .hasMessageContaining("already exists");
    }
}