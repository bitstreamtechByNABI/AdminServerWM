package worker.meet.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import worker.meet.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

}
