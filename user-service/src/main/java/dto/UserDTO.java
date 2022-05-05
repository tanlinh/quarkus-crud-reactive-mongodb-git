package dto;

import dto.enumm.ERole;
import entity.Address;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {

    private ObjectId id;
    private String email;
    private String phoneNumber;
    private Boolean status;

    private String password;
    private Set<ERole> roles;
    private String role;
    private String userName;
    @NotBlank(message="username may not be blank")
    private String name;
    private Set<Address> addresses;
    private String file;
    private String testMapper;
    private List<ProductDTO> productList;
}
