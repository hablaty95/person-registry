package com.example.personregistry.controller;

import com.example.personregistry.model.Person;
import com.example.personregistry.service.PersonService;
import com.example.personregistry.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllPersons_thenReturnJsonArray() throws Exception {
        // given
        Person person1 = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        Person person2 = new Person("Jane", "Smith", LocalDate.of(1985, 5, 15));
        List<Person> persons = Arrays.asList(person1, person2);

        when(personService.getAllPersons()).thenReturn(persons);

        // when & then
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void whenGetPersonById_thenReturnPerson() throws Exception {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personService.getPersonById(1L)).thenReturn(person);

        // when & then
        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void whenGetPersonByIdNotFound_thenReturn404() throws Exception {
        // given
        when(personService.getPersonById(1L)).thenThrow(new ResourceNotFoundException("Person not found with id: 1"));

        // when & then
        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreatePerson_thenReturnCreatedPerson() throws Exception {
        // given
        Person person = new Person("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personService.createPerson(any(Person.class))).thenReturn(person);
        when(personService.personExists(any(), any(), any())).thenReturn(false);

        // when & then
        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void whenDeletePerson_thenReturnNoContent() throws Exception {
        // given
        doNothing().when(personService).deletePerson(1L);

        // when & then
        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNoContent());
    }
}