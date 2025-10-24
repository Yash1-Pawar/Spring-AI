package com.ai.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ai.model.AiRequest;
import com.ai.model.AiResponse;
import com.ai.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/ai")
public class Controller {

	public ChatService chatService;

	public Controller(ChatService chatService) {
		this.chatService = chatService;
	}

	@Operation(summary = "Query AI with simple query parameter")
	@GetMapping("/chat")
	public ResponseEntity<String> aiQuery(String query,
			 @Parameter(
			            description = "Name of the AI model to use",
			            schema = @Schema(allowableValues = {"ollama", "openai"})
			        )
			@RequestParam(defaultValue = "ollama") String model) {
		String queryAi = this.chatService.queryAi(query, model);
		return ResponseEntity.ok(queryAi);
	}

	@Operation(summary = "Query AI with Response Entity")
	@PostMapping("/chat/entity")
	public ResponseEntity<?> aiQueryWithEntity(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AiRequest.class))) 
			@RequestBody AiRequest aiRequest) {
		AiResponse queryAiWithEntity = null;
		try {
			queryAiWithEntity = this.chatService.queryAiWithEntity(aiRequest.prompt(), aiRequest.model().name());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage() + ". Supported models are 'ollama' and 'openai'.");
		}
		return ResponseEntity.ok(queryAiWithEntity);
	}

	@Operation(summary = "Query AI with Prompt Templating")
	@PostMapping("/chat/prompt-templating")
	public ResponseEntity<?> aiQueryWithPromptTemplating(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AiRequest.class))) 
			@RequestBody AiRequest aiRequest) {
		String response = null;
		try {
			response = this.chatService.queryAiWithPromptTemplating(aiRequest.prompt(), aiRequest.model().name());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage() + ". Supported models are 'ollama' and 'openai'.");
		}
		return ResponseEntity.ok(response);
	}

}
