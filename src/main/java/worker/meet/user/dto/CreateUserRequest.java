package worker.meet.user.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

	private String username;
	private String password;
	private String role = "USER"; 

}
