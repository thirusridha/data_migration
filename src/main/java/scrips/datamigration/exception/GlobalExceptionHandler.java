package scrips.datamigration.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import scrips.datamigration.domain.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
@ExceptionHandler({Exception.class})
private final ResponseEntity<Object> handleAllExceptions(Exception ex,WebRequest request){
	ErrorResponseDto apiError = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR);
	apiError.setMessage("Internal Server Error");
	apiError.setDebugMessage(ex.getLocalizedMessage());
	return buildResponceEntity(apiError);
}


@ExceptionHandler({FailedValidationException.class})
private final ResponseEntity<Object> handleValidationExceptions(Exception ex,WebRequest request){
	ErrorResponseDto apiError = new ErrorResponseDto(HttpStatus.BAD_REQUEST);
	apiError.setMessage(ex.getMessage());
	if(Objects.nonNull(ex.getCause()))
		apiError.setDebugMessage(ex.getCause().toString());
	return buildResponceEntity(apiError);
}

private ResponseEntity<Object> buildResponceEntity(ErrorResponseDto apiError){
return new ResponseEntity<Object>(apiError, apiError.getStatus());
}
}
