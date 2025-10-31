package worker.meet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeApplicationDTO {
	
	private String candidateName;
	private String email;
	private String applicationNo;
	private String applicationStatus = "Pending";

	public EmployeeApplicationDTO(String candidateName, String email, String applicationNo) {
		this.candidateName = candidateName;
		this.email = email;
		this.applicationNo = applicationNo;
	}

}
