package worker.meet.login.scheduler;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import worker.meet.login.entity.OtpEntity;

@Component
public class OtpCleanupScheduler {

	private final Map<String, OtpEntity> otpCache;

	public OtpCleanupScheduler(Map<String, OtpEntity> otpCache) {
		this.otpCache = otpCache;
	}

	@Scheduled(fixedRate = 10000) // every 10 seconds
	public void cleanExpiredOtps() {
		LocalDateTime now = LocalDateTime.now();
		otpCache.entrySet().removeIf(entry -> entry.getValue().getExpiryTime().isBefore(now));
		System.out.println("Expired OTPs cleaned at " + now);
	}

}
