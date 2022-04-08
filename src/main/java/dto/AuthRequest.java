package dto;

import dto.enumm.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 *
 * @author ard333
 */
@NoArgsConstructor @AllArgsConstructor @ToString
@Data
public class  AuthRequest {
	
	private String username;
	private String password;
	private Set<ERole> roles;
}
