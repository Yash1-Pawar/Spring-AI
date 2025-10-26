package com.ai.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AiTools {
	
	public static final Logger log = LoggerFactory.getLogger(AiTools.class);
	
	private final RestClient restClient;

    public AiTools(RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
    	log.info("Calling getCurrentDateTime Tool");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
    
    @Tool(description = "Get the current date and time in a specified timezone")
    String getCurrentDateTimeByTimeZone(@ToolParam(description = "Time Zone of the location") String timezone) {
    	log.info("Calling getCurrentDateTimeByTimeZone Tool with timezone: {}", timezone);
		return LocalDateTime.now().atZone(ZoneId.of(timezone)).toString();
	}
    
    @Tool(description = "Get the current weather for a specified city")
    ResponseEntity<String> getCurrentWeather(@ToolParam(description = "City name to get the weather for") String city) {
    	log.info("Calling getCurrentWeather Tool with city: {}", city);
    	ResponseEntity<String> response = this.restClient
    			.get()
    			.uri(uriBuilder -> uriBuilder
    					.path("/v1/current.json")
						.queryParam("lang", "en")
						.queryParam("key", "90cd847ef0c14399b0473522252610")
						.queryParam("q", city).build())
    			.retrieve()
    			.toEntity(String.class);
    	log.info("Weather API Response: {}", response);
		return response;
	}
    
	
}
