package entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

import java.util.List;

@Data
public class Product extends ReactivePanacheMongoEntity {

    private String productName;
    private Double price;
    private Integer quantity;
    private List<Order> orderList;
}
