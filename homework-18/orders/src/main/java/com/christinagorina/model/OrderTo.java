package com.christinagorina.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTo {

    public String orderIdentifier;

    public String idempotencyKey;

    public State state;

    public Long buyerId;

    public String product;

}
