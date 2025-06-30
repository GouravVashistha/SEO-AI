package com.seo_ai_service.config;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.ollama.OllamaChatClient;

@Configuration
public class AIConfig {

    @Bean
    public OllamaChatClient ollamaChatClient() {
        OllamaApi ollamaApi = new OllamaApi("http://localhost:11434");
        return new OllamaChatClient(ollamaApi);
    }
}
