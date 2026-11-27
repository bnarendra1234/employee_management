package com.learning.employeemanagementsystem.exception;

import com.learning.employeemanagementsystem.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleEmployeeNotFound(EmployeeNotFoundException exception) {
		return error(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ApiResponse<Void>> handleDuplicateEmail(DuplicateEmailException exception) {
		return error(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(InvalidArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidArgument(InvalidArgumentException exception) {
		return error(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.findFirst()
				.orElse("Request validation failed");
		return error(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception exception) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	}

	private ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(new ApiResponse<Void>(message, null, status.value()));
	}
}
