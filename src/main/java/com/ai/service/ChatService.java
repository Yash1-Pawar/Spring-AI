package com.ai.service;

import com.ai.model.AiRequest;
import com.ai.model.AiResponse;

public interface ChatService {

	String queryAi(String query, String model, String userId);

	AiResponse queryAiWithEntity(AiRequest aiRequest);

	String queryAiWithPromptTemplating(AiRequest aiRequest);

	String queryAiWithRag(String query, String model, String userId);
}
