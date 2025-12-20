package com.example.College_Management_Portal.Service;

import org.springframework.stereotype.Service;

import dev.langchain4j.model.openai.OpenAiChatModel;


@Service
public class LlmService {
    
    private final OpenAiChatModel model;

    

    public LlmService(OpenAiChatModel model){
        this.model = model;
    }

    public String generateText(String prompt){
        return model.generate(prompt);
    } 

    
         
}
