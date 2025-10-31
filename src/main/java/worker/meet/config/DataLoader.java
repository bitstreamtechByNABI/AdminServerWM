package worker.meet.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import worker.meet.user.service.UserService;

//@Component
public class DataLoader {
	
	  private final UserService userService;

	    public DataLoader(UserService userService) {
	        this.userService = userService;
	    }

	    @EventListener(ApplicationReadyEvent.class)
	    public void loadData() {
	        // Only create if it doesn’t exist
	        try {
	            userService.createUser("Fatima", "fatima123", "ADMIN");
	            System.out.println("Default admin created: nabi/alam");
	        } catch (RuntimeException e) {
	            System.out.println("Default admin already exists");
	        }
	    }

}
