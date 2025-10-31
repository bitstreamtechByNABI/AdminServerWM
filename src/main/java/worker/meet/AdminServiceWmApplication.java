package worker.meet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "worker.meet")
@EntityScan(basePackages = {
        "worker.meet.user.model",
        "worker.meet.login.entity",
        "worker.meet.model" // if Employee entity lives here
})
@EnableJpaRepositories(basePackages = {
        "worker.meet.repository",
        "worker.meet.login.repository",
        "worker.meet.user.repository"
})
public class AdminServiceWmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceWmApplication.class, args);
        System.out.println("✅ Admin Server Started Successfully on Port 8085");
    }
}
