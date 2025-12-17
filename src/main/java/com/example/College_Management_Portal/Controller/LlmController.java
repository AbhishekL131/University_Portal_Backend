package com.example.College_Management_Portal.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.College_Management_Portal.Service.LlmService;

@RestController
@RequestMapping("/api/LLM")
public class LlmController {

    private final LlmService llmService;

    public LlmController(LlmService llmService){
        this.llmService = llmService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String,String> query){
        String prompt = query.get("Prompt");
        String result = llmService.generateText(prompt);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
}
