package com.product.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

@Data
public class User extends ReactivePanacheMongoEntity {

    private String name;
    private String email;

    private String userName;

}

