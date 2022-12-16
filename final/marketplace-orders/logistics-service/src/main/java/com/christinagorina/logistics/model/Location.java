package com.christinagorina.logistics.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "location")
public class Location {

    @Id
    private String id;
    private List<Float> coordinates;
    private String type;

}
