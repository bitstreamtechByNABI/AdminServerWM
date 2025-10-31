package worker.meet.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import worker.meet.user.model.User;
import worker.meet.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	  @Autowired
	    private UserRepository userRepository;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        return org.springframework.security.core.userdetails.User.builder()
	                .username(user.getUsername())
	                .password(user.getPassword())
	                .roles(user.getRole().getName())
	                .build();
	    }
}
