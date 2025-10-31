package worker.meet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import worker.meet.login.dto.LoginResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<LoginResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
	        return new ResponseEntity<>(new LoginResponseDTO(ex.getMessage(), false), HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<LoginResponseDTO> handleRuntime(RuntimeException ex) {
	        return new ResponseEntity<>(new LoginResponseDTO(ex.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<LoginResponseDTO> handleGeneral(Exception ex) {
	        return new ResponseEntity<>(new LoginResponseDTO("Unexpected error: " + ex.getMessage(), false),
	                HttpStatus.INTERNAL_SERVER_ERROR);
	    }
}
