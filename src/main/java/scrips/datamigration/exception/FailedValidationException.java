package scrips.datamigration.exception;

public class FailedValidationException extends RuntimeException {

	public FailedValidationException(String errorMessage, Throwable cause) {
		super(errorMessage,cause);
	}
	public FailedValidationException(String errorMessage) {
		super(errorMessage);
	}
}
