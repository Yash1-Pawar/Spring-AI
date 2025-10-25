package com.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.ai.service.CustomLoggerAdvisor;
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
	ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory, VectorStore vectorStore) {
		
//		Chat Memory configured via Advisors
		MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();	
		
		return ChatClient.builder(openAiChatModel)
//				.defaultAdvisors(new CustomLoggerAdvisor())
				.defaultAdvisors(new SimpleLoggerAdvisor(), messageChatMemoryAdvisor)
				.defaultSystem(defaultSystemPrompt)
				.defaultOptions(OpenAiChatOptions.builder()
						.maxCompletionTokens(200)
						.temperature(0.7)
						.build())
				.build();
	}
	
	
	
}
