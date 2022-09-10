package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public Long id;

    private String name;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

}