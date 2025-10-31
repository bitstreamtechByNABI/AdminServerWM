package worker.meet.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import worker.meet.login.dto.EmailOtpRequestDTO;
import worker.meet.login.dto.LoginResponseDTO;
import worker.meet.login.dto.OtpVerifyRequestDTO;
import worker.meet.login.service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {
	
    @Autowired
    private LoginService loginService;
    
	@PostMapping("/send-otp")
	public ResponseEntity<LoginResponseDTO> sendOtp(@RequestBody EmailOtpRequestDTO request) {
		try {
			LoginResponseDTO response = loginService.sendOtp(request);
			if (response.isSuccess()) {
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new LoginResponseDTO(e.getMessage(), false), HttpStatus.BAD_REQUEST);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(new LoginResponseDTO(e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(new LoginResponseDTO("Something went wrong: " + e.getMessage(), false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	 @PostMapping("/verify-otp")
	    public ResponseEntity<LoginResponseDTO> verifyOtp(@RequestBody OtpVerifyRequestDTO request) {
	        try {
	            LoginResponseDTO response = loginService.verifyOtp(request);
	            if (response.isSuccess()) {
	                // ✅ OTP verified successfully
	                return new ResponseEntity<>(response, HttpStatus.OK);
	            } else {
	                // ⚠️ Invalid or expired OTP
	                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	            }
	        } catch (IllegalArgumentException e) {
	            return new ResponseEntity<>(new LoginResponseDTO(e.getMessage(), false), HttpStatus.BAD_REQUEST);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(new LoginResponseDTO(e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
	        } catch (Exception e) {
	            return new ResponseEntity<>(
	                    new LoginResponseDTO("Something went wrong: " + e.getMessage(), false),
	                    HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
