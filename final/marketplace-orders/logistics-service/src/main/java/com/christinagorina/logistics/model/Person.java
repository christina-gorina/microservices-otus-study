package com.christinagorina.logistics.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "persons")
public class Person {

    @Id
    private String id;
    private String name;
    private Location location;

    public Person(String name) {
        this.name = name;
    }

}
