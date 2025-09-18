package com.example.personregistry.repository;

import com.example.personregistry.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByLastNameIgnoreCase(String lastName);

    List<Person> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    @Query("SELECT p FROM Person p LEFT JOIN FETCH p.addresses WHERE p.id = :id")
    Optional<Person> findByIdWithAddresses(@Param("id") Long id);

    boolean existsByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);
}