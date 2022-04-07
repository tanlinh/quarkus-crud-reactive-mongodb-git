package entity;

import dto.enumm.ERole;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.Username;
import lombok.Data;

import java.util.Set;

@Data
public class User extends ReactivePanacheMongoEntity {

    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Boolean status;

    @Username
    private String userName;
    @Password
    private String password;
    @Roles
    private Set<ERole> roles;

    @Roles
    private String role;

    public User() {
    }

}

