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

@RestController
@RequestMapping("/api/v1/ai")
public class Controller {

	public ChatService chatService;

	public Controller(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping("/chat")
	public ResponseEntity<String> aiQuery(@RequestParam("q") String query,
			@RequestParam(defaultValue = "ollama") String model) {
		String queryAi = this.chatService.queryAi(query, model);
		return ResponseEntity.ok(queryAi);
	}

	@PostMapping("/chat/entity")
	public ResponseEntity<?> aiQueryWithEntity(@RequestBody AiRequest aiRequest) {
		AiResponse queryAiWithEntity = null;
		try {
			queryAiWithEntity = this.chatService.queryAiWithEntity(aiRequest.prompt(), aiRequest.model());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage() + ". Supported models are 'ollama' and 'openai'.");
		}
		return ResponseEntity.ok(queryAiWithEntity);
	}

}
