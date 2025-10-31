package worker.meet.com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import worker.meet.com.service.EmployeeService;
import worker.meet.dto.EmployeeApplicationDTO;
import worker.meet.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private  EmployeeRepository employeeRepository;

	@Override
	public List<EmployeeApplicationDTO> getAllEmployeeApplications() {
		  List<Object[]> results = employeeRepository.fetchEmployeeApplications();

	        return results.stream().map(row -> {
	            String name = row[0] != null ? row[0].toString() : "";
	            String email = row[1] != null ? row[1].toString() : "";
	            String applicationNo = row[2] != null ? row[2].toString() : "";
	            return new EmployeeApplicationDTO(name, email, applicationNo);
	        }).collect(Collectors.toList());
	}
	
	
}
