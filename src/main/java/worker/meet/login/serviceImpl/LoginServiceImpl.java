package worker.meet.login.serviceImpl;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import worker.meet.enam.OtpConfigEnum;
import worker.meet.login.dto.EmailOtpRequestDTO;
import worker.meet.login.dto.LoginResponseDTO;
import worker.meet.login.dto.OtpVerifyRequestDTO;
import worker.meet.login.entity.OtpEntity;
import worker.meet.login.repository.OtpRepository;
import worker.meet.login.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final SecureRandom secureRandom = new SecureRandom();

    // Redis key patterns
    private String otpKey(String email) { return "otp:" + email; }
    private String attemptKey(String email) { return "otp:attempt:" + email; }
    private String blockKey(String email) { return "otp:block:" + email; }

    /**
     * Send OTP via email and save in Redis
     */
    @Override
    public LoginResponseDTO sendOtp(EmailOtpRequestDTO request) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            return new LoginResponseDTO("Invalid email request", false);
        }

        String email = request.getEmail().trim();
        

        // Check if user is blocked
        if (Boolean.TRUE.equals(redis.hasKey(blockKey(email)))) {
            Long ttl = redis.getExpire(blockKey(email), TimeUnit.MINUTES);
            return new LoginResponseDTO("Your account is temporarily blocked. Try again after "
                    + (ttl != null ? ttl : OtpConfigEnum.BLOCK_TTL_MINUTES) + " minutes.", false);
        }

        // Generate new OTP
        String otp = generateNumericOtp(OtpConfigEnum.OTP_LENGTH.getValue());
        String otpBase64 = Base64.getEncoder().encodeToString(otp.getBytes(StandardCharsets.UTF_8));

        // Save OTP to Redis with TTL
      
        redis.opsForValue().set(otpKey(email), otpBase64, Duration.ofMinutes(OtpConfigEnum.BLOCK_TTL_MINUTES.getValue()));
        redis.delete(attemptKey(email)); // reset attempts

        // --- Save in DB (optional for audit) ---
        // You can still persist minimal info in DB if required
        // Example:
         OtpEntity entity = new OtpEntity();
         entity.setEmail(email);
         entity.setUserId(request.getUserId().trim());
         entity.setUserName(request.getUserName().trim());
         entity.setOtpBase64(otpBase64);
         entity.setExpiryTime(LocalDateTime.now().plusMinutes(OtpConfigEnum.OTP_TTL_MINUTES.getValue()));
         entity.setVerified(false);
         otpRepository.save(entity);

        try {
            // Prepare Email HTML
            String htmlBody = "<html><body>"
                    + "<div style='font-family:Arial,sans-serif;padding:20px;'>"
                    + "<h3>🔐 Login OTP Verification - Workers Meet</h3>"
                    + "<p>Hi <b>" + request.getUserName() + "</b>,</p>"
                    + "<p>Your one-time password (OTP) for login is:</p>"
                    + "<div style='text-align:center;margin:15px 0;'>"
                    + "<span style='font-size:24px;background-color:#2a9df4;color:white;padding:10px 20px;border-radius:8px;'>"
                    + otp + "</span></div>"
                    + "<p>This OTP will expire in <b>5 minutes</b>.</p>"
                    + "<p>If you didn’t request this, please ignore this email.</p>"
                    + "<br><small>© 2025 Workers Meet</small></div></body></html>";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper =
                    new org.springframework.mail.javamail.MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(email);
            helper.setSubject("Your Login OTP - Workers Meet");
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);

            return new LoginResponseDTO("OTP sent successfully to " + email, true);
        } catch (Exception e) {
            return new LoginResponseDTO("Failed to send OTP: " + e.getMessage(), false);
        }
    }

    /**
     * Verify OTP logic with 3 attempts + 30 min block
     */
    @Override
    public LoginResponseDTO verifyOtp(OtpVerifyRequestDTO request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            return new LoginResponseDTO("Invalid request.", false);
        }

        email = email.trim();

        // 1️⃣ Check block
        if (Boolean.TRUE.equals(redis.hasKey(blockKey(email)))) {
            Long ttl = redis.getExpire(blockKey(email), TimeUnit.MINUTES);
            return new LoginResponseDTO("You are blocked. Try again after "
                    + (ttl != null ? ttl : OtpConfigEnum.BLOCK_TTL_MINUTES.getValue()) + " minutes.", false);
        }


        // 2️⃣ Get stored OTP
        String storedOtpBase64 = redis.opsForValue().get(otpKey(email));
        if (storedOtpBase64 == null) {
            return new LoginResponseDTO("OTP expired or not found. Please request again.", false);
        }

        String decodedOtp;
        try {
            decodedOtp = new String(Base64.getDecoder().decode(storedOtpBase64), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            redis.delete(otpKey(email));
            return new LoginResponseDTO("Corrupted OTP data. Please request again.", false);
        }

        // 3️⃣ Verify OTP
        if (decodedOtp.equals(otp)) {
            redis.delete(otpKey(email));
            redis.delete(attemptKey(email));
            return new LoginResponseDTO("Login successful!", true);
        }

        // 4️⃣ Wrong OTP → increment attempts
        Long attempts = redis.opsForValue().increment(attemptKey(email));
        redis.expire(attemptKey(email), OtpConfigEnum.OTP_TTL_MINUTES.getValue(), TimeUnit.MINUTES);


        int MAX_ATTEMPTS = OtpConfigEnum.MAX_ATTEMPTS.getValue();
        int BLOCK_TTL_MINUTES = OtpConfigEnum.BLOCK_TTL_MINUTES.getValue();

        if (attempts >= MAX_ATTEMPTS) {
            redis.opsForValue().set(blockKey(email), "blocked", BLOCK_TTL_MINUTES, TimeUnit.MINUTES);
            redis.delete(otpKey(email));
            redis.delete(attemptKey(email));
            return new LoginResponseDTO("Too many wrong attempts. You are blocked for "
                    + BLOCK_TTL_MINUTES + " minutes.", false);
        }

        long remaining = MAX_ATTEMPTS - attempts;
        return new LoginResponseDTO("Invalid OTP. Attempts left: " + remaining, false);
    }

    // Utility: generate numeric OTP
    private String generateNumericOtp(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
}
