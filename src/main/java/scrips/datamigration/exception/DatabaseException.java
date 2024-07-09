package scrips.datamigration.exception;

import lombok.Getter;
import lombok.Setter;


public class DatabaseException extends RuntimeException{
	
	public DatabaseException(String errorMessage, Throwable cause) {
		super(errorMessage,cause);
	}
	public DatabaseException(String errorMessage) {
		super(errorMessage);
	}
	
	
}
