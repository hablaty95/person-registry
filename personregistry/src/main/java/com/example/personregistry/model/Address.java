package com.example.personregistry.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ADDRESS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"person_id", "address_type"}))
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @NotNull(message = "Address type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 20)
    private AddressType addressType;

    @NotBlank(message = "Country is mandatory")
    @Column(nullable = false, length = 100)
    private String country;

    @NotBlank(message = "City is mandatory")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Zip code is mandatory")
    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @NotBlank(message = "Street is mandatory")
    @Column(nullable = false, length = 200)
    private String street;

    @Column(name = "house_number", length = 20)
    private String houseNumber;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors, getters, setters...


    public Person getPerson() {
        return person;
    }

    public String getCity() {
        return city;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}