package com.springAI.chatAI.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springAI.chatAI.service.ChatAIService;

@RestController
@RequestMapping("/openAI")
public class ChatOpenAIController {
	
	@Autowired
	private ChatAIService chatAIService;
	
	
		
	@GetMapping("/api/chat/{msg}")
	public @Nullable String chatWithOpenAI(@PathVariable String msg) {
		return chatAIService.chatWithAI(msg);
	}

}
