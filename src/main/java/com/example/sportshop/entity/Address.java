package com.example.sportshop.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class Address {
    @NotNull(message = "Street cant be empty!")
    @NotBlank(message = "Street cant be empty!")
    private String street;

    @NotNull(message = "House cant be empty!")
    @NotBlank(message = "House cant be empty!")
    private String house;
    private String apartment;

    @NotNull(message = "Country cant be empty!")
    @NotBlank(message = "Country cant be empty!")
    private String city;

    @NotNull(message = "State cant be empty!")
    @NotBlank(message = "State cant be empty!")
    private String state;

    private String zip;

    @NotNull(message = "Country cant be empty!")
    @NotBlank(message = "Country cant be empty!")
    private String country;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(country).append(city).append(state).append(street).append(house);
        if (zip != null && !zip.isEmpty()) sb.append(zip);
        if (apartment != null && !apartment.isEmpty()) sb.append(apartment);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(street, address.street) && Objects.equals(house, address.house)
                && Objects.equals(apartment, address.apartment)
                && Objects.equals(city, address.city) && Objects.equals(state, address.state)
                && Objects.equals(zip, address.zip) && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, house, apartment, city, state, zip, country);
    }
}
