package worker.meet.user.service;

import worker.meet.user.model.User;



public interface UserService {

	User createUser(String username, String password, String roleName);
}
