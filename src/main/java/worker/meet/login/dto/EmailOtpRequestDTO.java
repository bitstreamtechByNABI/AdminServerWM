package worker.meet.login.dto;

import lombok.Data;

@Data
public class EmailOtpRequestDTO {

	private String userId;
	private String userName;
	private String email;
}
