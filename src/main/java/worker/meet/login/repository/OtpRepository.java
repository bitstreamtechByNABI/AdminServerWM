package worker.meet.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import worker.meet.login.entity.OtpEntity;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
	
	 Optional<OtpEntity> findTopByEmailOrderByIdDesc(String email);

}
