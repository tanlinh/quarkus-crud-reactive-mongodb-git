package dto;

import dto.enumm.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;


@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class AuthResponse {
	
	private String token;
	private String userName;
	private String email;
	private Set<ERole> roles;
}
