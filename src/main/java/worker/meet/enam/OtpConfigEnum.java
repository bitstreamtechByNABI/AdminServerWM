package worker.meet.enam;

public enum OtpConfigEnum {
	
	
	   OTP_TTL_MINUTES(5),
	    BLOCK_TTL_MINUTES(30),
	    MAX_ATTEMPTS(3),
	    OTP_LENGTH(6);
	
	  private final int value;

	    OtpConfigEnum(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }

}
