package com.example.tacocloud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Taco_Order")
public class Order {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime placedAt;
    @NotBlank(message = "Name is required")
    @Column(name = "deliveryName")
    private String name;
    @NotBlank(message = "Street is required")
    @Column(name = "deliveryStreet")
    private String street;
    @NotBlank(message = "City is required")
    @Column(name = "deliveryCity")
    private String city;
    @NotBlank(message = "State is required")
    @Column(name = "deliveryState")
    private String state;
    @NotBlank(message = "Zip code is required")
    @Column(name = "deliveryZip")
    private String zip;
    //    @ManyToMany(targetEntity = Taco.class)
    @ManyToMany
    @JoinTable(
            name = "Taco_Order_Tacos",
            joinColumns = @JoinColumn(name = "tacoOrder"),
            inverseJoinColumns = @JoinColumn(name = "taco")
    )
    private List<Taco> tacos = new ArrayList<>();

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
            message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    @PrePersist
    void placedAt() {
        this.placedAt = LocalDateTime.now();
    }

    public void addDesign(Taco saved) {
        this.tacos.add(saved);
    }
}
