package entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

@Data
public class Address extends ReactivePanacheMongoEntity {

    private String province;
    private String district;
    private String town;
}
