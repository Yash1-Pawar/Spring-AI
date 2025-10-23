package com.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.ai.utility.ChatClientFactory;

import com.ai.model.AiResponse;
import com.ai.utility.AiModel;

@Service
public class ChatServiceImpl implements ChatService {
	
	public static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
	
	public ChatClientFactory chatClientFactory;
	
	public ChatServiceImpl(ChatClientFactory aiFactory) {
		this.chatClientFactory = aiFactory;
	}
	
	@Override
	public String queryAi(String query, String model) {
		ChatClient chatClient = this.getChatClient(model);
		var queryResponse = chatClient.prompt().user(query).call().content();
		return queryResponse;
	}
	
	@Override
	public AiResponse queryAiWithEntity(String query, String model) {
		ChatClient chatClient = this.getChatClient(model);
		Prompt prompt = new Prompt(query);
//		var response = chatClient
//				.prompt(prompt)
//				.call()
//				.entity(AiResponse.class);
		var response = chatClient
				.prompt(prompt)
				.call()
				.entity(AiResponse.class);
		
		log.info("AI Response Entity: {}", response);
		
		return response;
	}
	
	public ChatClient getChatClient(String model) {
		ChatClient chatClient = null;
		if (model.equalsIgnoreCase("ollama")) {
			log.info("Using Ollama Chat Client");
			chatClient = this.chatClientFactory.getChatClient(AiModel.OLLAMA);
		} else if (model.equalsIgnoreCase("openai")) {
			log.info("Using OpenAI Chat Client");
			chatClient = this.chatClientFactory.getChatClient(AiModel.OPENAI);
		} else {
			log.warn("===== Model not recognized =====");
			throw new RuntimeException("Unsupported AI Model: " + model);
		}
		return chatClient;
	}
	
}
