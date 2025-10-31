package worker.meet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import worker.meet.user.model.User;

public interface EmployeeRepository extends JpaRepository<User, Long> {
	
				@Query(value = "SELECT gem.candidate_name AS candidateName, " + "gem.email AS email, "
						+ "apno.application_no AS applicationNo " + "FROM gm_employees_master gem "
						+ "LEFT JOIN addresses a ON gem.id = a.employee_id " + "LEFT JOIN reg_documents rd ON rd.address_id = a.id "
						+ "LEFT JOIN application_numbers apno ON rd.id = apno.document_id "
						+ "ORDER BY apno.created_date DESC", nativeQuery = true)
				List<Object[]> fetchEmployeeApplications();

}
