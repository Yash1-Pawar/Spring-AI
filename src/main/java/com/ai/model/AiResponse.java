package com.ai.model;

public record AiResponse (
	String content,
	int totalTokensUsed,
	double costIncurred,
	String modelUsed,
	String responseTime
) {}
