package dto;

import dto.enumm.ERole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UserDTO {

    private String email;
    private String address;
    private String phoneNumber;
    private Boolean status;

    private String password;
    private Set<ERole> roles;
    private String role;
    private String userName;
    @NotBlank(message="username may not be blank")
    private String name;
}
