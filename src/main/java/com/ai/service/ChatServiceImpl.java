package com.ai.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.ai.utility.ChatClientFactory;
import com.ai.model.AiRequest;
import com.ai.model.AiResponse;
import com.ai.utility.AiModel;

@Service
public class ChatServiceImpl implements ChatService {
	
	public static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
	
	public ChatClientFactory chatClientFactory;
	public VectorStore vectorStore;
	
	public ChatServiceImpl(ChatClientFactory aiFactory, VectorStore vectorStore) {
		this.chatClientFactory = aiFactory;
		this.vectorStore = vectorStore;
	}
	
	@Override
	public String queryAi(String query, String model, String userId) {
		ChatClient chatClient = this.getChatClient(model);
		var queryResponse = chatClient
				.prompt()
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
				.user(query)
				.call()
				.content();
		return queryResponse;
	}
	
	@Override
	public AiResponse queryAiWithEntity(AiRequest aiRequest) {
		ChatClient chatClient = this.getChatClient(aiRequest.model().name());
//		Prompt prompt = new Prompt(query);
//		var response = chatClient
//				.prompt(prompt)
//				.call()
//				.entity(AiResponse.class);
		var response = chatClient
				.prompt()
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, aiRequest.userId()))
				.user(aiRequest.prompt())
				.call()
				.entity(AiResponse.class);
		
		log.info("AI Response Entity: {}", response);
		
		return response;
	}
	
	@Override
	public String queryAiWithPromptTemplating(AiRequest aiRequest) {
		ChatClient chatClient = this.getChatClient(aiRequest.model().name());
		
		PromptTemplate promptTemplate = PromptTemplate.builder()
				.renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
				.template("""
						You are a helpful AI assistant. Please answer the following question:
						{user_query}
						""")
				.build();
		String userPrompt = promptTemplate.render(Map.of("user_query", aiRequest.prompt()));
		
		
		SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
				.template("You are an expert in Coding. Always provide {language} code examples only without explanation.")
				.build();
		String systemPrompt = systemPromptTemplate.render(Map.of("language", "Java"));
		
		var queryResponse = chatClient
				.prompt()
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, aiRequest.userId()))
				.user(userPrompt)
				.system(systemPrompt)
				.call()
				.content();
		
		return queryResponse;
	}
	
	@Override
	public String queryAiWithRag(String query, String model, String userId) {
		ChatClient chatClient = this.getChatClient(model);
		
//		Implementing Advanced RAG with Modular Advisors
		var retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
			
				.queryTransformers(
						RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate().clone()).build(),
						TranslationQueryTransformer.builder().chatClientBuilder(chatClient.mutate().clone()).targetLanguage("english").build()
				)
				
				.documentRetriever(
						VectorStoreDocumentRetriever.builder()
							.vectorStore(vectorStore)
							.similarityThreshold(0.73)
						    .topK(10)
							.build()
				)
				
				.documentJoiner(new ConcatenationDocumentJoiner())
				.queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
			
			.build();
		
		var queryResponse = chatClient
				.prompt()
				.advisors(retrievalAugmentationAdvisor)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
				.user(query)
				.call()
				.content();
		return queryResponse;
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
