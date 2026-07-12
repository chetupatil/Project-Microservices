package com.springAI.chatAI.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springAI.chatAI.model.ChatAIModel;
import com.springAI.chatAI.repository.ChatAIRepository;

@Service
public class ChatAIService {

	private ChatClient chatClient;
	
	@Autowired
	private ChatAIRepository chatAIRepository;

	ChatAIService(ChatClient chatClient){
		this.chatClient = chatClient;
	}


	public String chatWithAI(String msg) {
		String res = this.chatClient.prompt().user(msg).call().content();
		
		
		
		ChatAIModel chatAIModel = new ChatAIModel();
		chatAIModel.setMessage(res);
		chatAIModel.setRoles("Developer");
		chatAIModel.setCreated(null);
		chatAIModel.setModified(null);
		chatAIModel.setLlm_model(null);
		chatAIModel.setCreated_by(null);
		chatAIModel.setModified_by(null);
		chatAIModel.setMode_llm(null);
		chatAIRepository.save(chatAIModel);
		
		return res;
	}


}
