package com.christinagorina.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reserve")
public class Reserve {

    @Id
    private String id;
    private UUID orderUUID;
    private UUID productItemUUID;
    private Integer count;

}
