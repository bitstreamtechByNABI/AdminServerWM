package worker.meet.user.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.NoArgsConstructor;
import worker.meet.user.model.Role;
import worker.meet.user.model.User;
import worker.meet.user.repository.RoleRepository;
import worker.meet.user.repository.UserRepository;
import worker.meet.user.service.UserService;

@Data
@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Override
	    public User createUser(String username, String password, String roleName) {
	        // 1️⃣ Check if username exists
	        Optional<User> existingUserOpt = userRepository.findByUsername(username);

	        if (existingUserOpt.isPresent()) {
	            User existingUser = existingUserOpt.get();

	            // If user exists and inactive → activate user
	            if (Boolean.FALSE.equals(existingUser.getUserActiveStatus())) {
	                existingUser.setUserActiveStatus(true);
	                existingUser.setUpdatedAt(LocalDateTime.now());
	                return userRepository.save(existingUser);
	            }

	            // If already active → no need to create again
	            throw new RuntimeException("Username already exists and is active");
	        }

	        // 2️⃣ Check if role exists, else create new one
	        Role role = roleRepository.findByName(roleName)
	            .orElseGet(() -> {
	                Role newRole = new Role();
	                newRole.setName(roleName);
	                return roleRepository.save(newRole);
	            });

	        // 3️⃣ Create new user
	        User user = new User();
	        user.setUsername(username);
	        user.setPassword(passwordEncoder.encode(password));
	        user.setRole(role);
	        user.setUserActiveStatus(true);

	        return userRepository.save(user);
	    }

			
}
