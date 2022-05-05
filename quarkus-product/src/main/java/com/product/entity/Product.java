package com.product.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

@Data
public class Product extends ReactivePanacheMongoEntity {

    private String productName;
    private Double price;
    private Integer quantity;
}
