package worker.meet.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import worker.meet.login.entity.OtpEntity;

@Configuration
public class OtpCacheConfig {

	  @Bean
	    public Map<String, OtpEntity> otpCache() {
	        return new ConcurrentHashMap<>();
	    }
}
