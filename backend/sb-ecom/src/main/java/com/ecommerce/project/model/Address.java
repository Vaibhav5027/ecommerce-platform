package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long addressId;
    @NotBlank
    @Size(max = 50, message = "Building name must not exceed 50 characters")
    private String buildingName;

    @NotBlank
    @Size(max = 100, message = "Street must not exceed 100 characters")
    private String street;

    @NotBlank
    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @NotBlank
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;


    @NotNull
    @Min(value = 100000, message = "Pin code must be at least 6 digits")
    @Max(value = 999999, message = "Pin code must not exceed 6 digits")
    private Integer pinCode;

    @ManyToMany(mappedBy = "address")
    private List<User> user=new ArrayList<>();

    public Address(String buildingName, String street, String state, String country, Integer pinCode) {
        this.buildingName = buildingName;
        this.street = street;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
