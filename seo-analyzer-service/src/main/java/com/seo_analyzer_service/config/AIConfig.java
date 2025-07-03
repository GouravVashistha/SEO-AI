package com.seo_analyzer_service.config;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Bean
    public OllamaChatClient ollamaChatClient() {
        OllamaApi ollamaApi = new OllamaApi("http://localhost:11434");
        return new OllamaChatClient(ollamaApi);
    }
}
