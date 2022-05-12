package entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

@Data
public class Order extends ReactivePanacheMongoEntity {

    private String status;
    private Double total;
    private String name;
    private Integer quantity;
}
