package com.ai.service;

import com.ai.model.AiResponse;

public interface ChatService {

	String queryAi(String query, String model);

	AiResponse queryAiWithEntity(String query, String model);

	String queryAiWithPromptTemplating(String query, String model);
}
