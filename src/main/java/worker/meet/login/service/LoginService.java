package worker.meet.login.service;

import worker.meet.login.dto.EmailOtpRequestDTO;
import worker.meet.login.dto.LoginResponseDTO;
import worker.meet.login.dto.OtpVerifyRequestDTO;

public interface LoginService {

	LoginResponseDTO sendOtp(EmailOtpRequestDTO request);

	LoginResponseDTO verifyOtp(OtpVerifyRequestDTO request);

}
