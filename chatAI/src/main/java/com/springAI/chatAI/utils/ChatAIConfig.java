package com.springAI.chatAI.utils;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ChatAIConfig {
	
	
	private ChatClient chatClient;

    @Bean
    @Profile("dev")
    ChatClient ChatClientConf(ChatModel chatModel) {
    	
    	this.chatClient = ChatClient.builder(chatModel).build();
    	
		return this.chatClient;
	}
	
	

}
