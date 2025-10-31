package worker.meet.login.dto;

import lombok.Data;

@Data
public class OtpVerifyRequestDTO {
    private String userId;
    private String email;
    private String otp;
    private String userName;

}
