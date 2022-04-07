package dto;

import dto.enumm.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Boolean status;

    private String userName;
    private String password;
    private Set<ERole> roles;
}
