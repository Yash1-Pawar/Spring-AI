package com.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.ai.utility.AppConstants;

import jakarta.annotation.PostConstruct;

@Configuration
public class AppConfig {

	@Value("${spring.ai.openai.api-key}")
	private String OPENAI_API_KEY;
	
	@Value("classpath:/SystemPrompt.txt")
	private Resource defaultSystemPrompt;

	@PostConstruct
	public void post() {
		System.err.println("Using OpenAI API Key: " + OPENAI_API_KEY);
	}
	
	@Bean(name = AppConstants.OPEN_AI_CHAT_CLIENT)
	ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
		return ChatClient.builder(openAiChatModel)
				.defaultSystem(defaultSystemPrompt)
				.defaultOptions(OpenAiChatOptions.builder()
						.maxCompletionTokens(200)
						.temperature(0.7)
						.build())
				.build();
	}
	
	@Bean(name = AppConstants.OLLAMA_CHAT_CLIENT)
	ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
		return ChatClient.builder(ollamaChatModel).build();
	}
	
}
