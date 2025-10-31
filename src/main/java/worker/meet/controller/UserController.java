package worker.meet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import worker.meet.user.dto.CreateUserRequest;
import worker.meet.user.model.User;
import worker.meet.user.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	 @PostMapping("/create")
	 public User createUser(@RequestBody CreateUserRequest request) {
	     return userService.createUser(
	             request.getUsername(),
	             request.getPassword(),
	             request.getRole()
	     );
	 }
	 
	 
	 
	 

}
