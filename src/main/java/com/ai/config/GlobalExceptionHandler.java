package com.ai.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ai.utility.AiModel;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidEnumValue(IllegalArgumentException ex) {
		String message = ex.getMessage();
		Object validValues = null;
		System.err.println("Exception Message: " + message);

		if (message != null) {
			if (message.contains("model"))
				validValues = AiModel.values();
		}

		return ResponseEntity.badRequest()
				.body(Map.of("error", message, "validValues", validValues == null ? List.of() : validValues));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("error", "Invalid request body");
		body.put("message", extractMessage(ex));
		return ResponseEntity.badRequest().body(body);
	}

	private String extractMessage(HttpMessageNotReadableException ex) {
		if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
			if (invalidFormat.getTargetType().isEnum()) {
				return "Invalid value. Allowed values: "
						+ Arrays.toString(invalidFormat.getTargetType().getEnumConstants());
			}
		}
		return ex.getMessage();
	}

}
