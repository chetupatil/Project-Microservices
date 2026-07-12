package com.springAI.chatAI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springAI.chatAI.model.ChatAIModel;

@Repository
public interface ChatAIRepository extends MongoRepository<ChatAIModel, String>{

}
