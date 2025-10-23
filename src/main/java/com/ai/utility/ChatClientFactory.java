package com.ai.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ChatClientFactory {
	
	public Map<String, ChatClient> chatClientMap = new HashMap<>();

	public ChatClientFactory(Map<String, ChatClient> chatClientMap) {
		this.chatClientMap = chatClientMap;
	}

	public ChatClient getChatClient(AiModel model) {
		switch (model) {
			case OLLAMA:
				return chatClientMap.get(AppConstants.OLLAMA_CHAT_CLIENT);
			case OPENAI:
				return chatClientMap.get(AppConstants.OPEN_AI_CHAT_CLIENT);
			default:
				throw new IllegalArgumentException("Unsupported AI Model: " + model);
		}
	}

}
