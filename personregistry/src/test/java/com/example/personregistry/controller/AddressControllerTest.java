package com.example.personregistry.controller;

import com.example.personregistry.model.Address;
import com.example.personregistry.model.Person;
import com.example.personregistry.model.AddressType;
import com.example.personregistry.service.AddressService;
import com.example.personregistry.exception.MaximumAddressException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateAddress_thenReturnCreatedAddress() throws Exception {
        // given
        Person person = new Person("John", "Doe", null);
        person.setId(1L);

        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);
        address.setCountry("Hungary");
        address.setCity("Budapest");

        when(addressService.createAddress(any(Address.class))).thenReturn(address);

        // when & then
        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country").value("Hungary"));
    }

    @Test
    void whenCreateAddressWithMaxAddresses_thenReturnBadRequest() throws Exception {
        // given
        Person person = new Person("John", "Doe", null);
        person.setId(1L);

        Address address = new Address();
        address.setPerson(person);
        address.setAddressType(AddressType.PERMANENT);

        when(addressService.createAddress(any(Address.class)))
                .thenThrow(new MaximumAddressException("Person can have maximum 2 addresses"));

        // when & then
        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest());
    }
}