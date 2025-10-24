package com.ai.model;

import com.ai.utility.AiModel;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiRequest(
		
		@Schema(description = "The prompt to send to the AI model", example = "Tell me a joke.")
		String prompt, 
		
		@Schema(description = "The AI model to use", example = "ollama")
		AiModel model,
		
		@Schema(description = "The user ID making the request", example = "123")
		String userId
		
		) {
}
