package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ard333
 */
@NoArgsConstructor @AllArgsConstructor @ToString
@Data
public class  AuthRequest {
	
	private String username;
	private String password;

}
