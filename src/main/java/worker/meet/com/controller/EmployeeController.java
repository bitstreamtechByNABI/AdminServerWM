package worker.meet.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import worker.meet.com.service.EmployeeService;
import worker.meet.dto.EmployeeApplicationDTO;




@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	  @GetMapping("/applications")
	    public ResponseEntity<List<EmployeeApplicationDTO>> getAllApplications() {
	        List<EmployeeApplicationDTO> applications = employeeService.getAllEmployeeApplications();
	        if (applications.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(applications);
	        }
	        return ResponseEntity.ok(applications);
	    }

}
