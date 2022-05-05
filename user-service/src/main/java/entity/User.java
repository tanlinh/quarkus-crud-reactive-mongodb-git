package entity;

import dto.enumm.ERole;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User extends ReactivePanacheMongoEntity {

    private String name;
    private String email;
    private Set<Address> addresses;
    private String phoneNumber;
    private Boolean status;

    private String userName;
    private String password;
    private Set<ERole> roles;
    private List<Product> productList;
    private String fileUpload;

}

