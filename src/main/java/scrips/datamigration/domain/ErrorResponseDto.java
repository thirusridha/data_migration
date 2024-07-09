package scrips.datamigration.domain;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto {
	private HttpStatus status;
	private LocalDate timeStamp;
	private String message;
	private String debugMessage;

	public ErrorResponseDto(HttpStatus status) {
		this.status = status;
		this.timeStamp = LocalDate.now();
	}

	public ErrorResponseDto(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
		this.timeStamp = LocalDate.now();
	}

	public ErrorResponseDto(String message) {
		this.message = message;
		this.timeStamp = LocalDate.now();
	}

	public ErrorResponseDto(HttpStatus status, Throwable ex) {
		this.status = status;
		this.debugMessage = ex.getLocalizedMessage();
		this.timeStamp = LocalDate.now();
	}
	public ErrorResponseDto(HttpStatus status, String message, Throwable ex) {
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
		this.timeStamp = LocalDate.now();
	}

}
