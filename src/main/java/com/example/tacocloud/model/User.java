package com.example.tacocloud.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Taco_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
}
