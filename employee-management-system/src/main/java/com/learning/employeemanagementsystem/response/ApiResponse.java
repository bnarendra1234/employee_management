package com.learning.employeemanagementsystem.response;

import java.time.Instant;

public class ApiResponse<T> {

	private final Instant timestamp;
	private final String message;
	private final T data;
	private final int status;

	public ApiResponse(String message, T data, int status) {
		this.timestamp = Instant.now();
		this.message = message;
		this.data = data;
		this.status = status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public int getStatus() {
		return status;
	}
}
