package com.seo_ai_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seo_ai_service.entity.SEOAnalysisRequest;
import com.seo_ai_service.entity.SEOAnalysisResponse;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;

@Service
public class SEOAIService {

    private final OllamaChatClient ollamaChatClient;

    public SEOAIService(OllamaChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    public SEOAnalysisResponse analyze(SEOAnalysisRequest request) {
        String prompt = """
        Analyze the SEO performance of this webpage based on the following raw data:

        Title: %s
        Meta Description: %s
        H1 Tags: %s
        H2 Tags: %s
        Total Images: %d
        Missing ALT tags: %d
        Page Speed (Desktop): %s
        Page Speed (Mobile): %s
        Mobile Friendly: %s
        SSL Enabled: %s
        Has Canonical: %s

        Return only a valid JSON response with SEO score and suggestions.
        """.formatted(
                request.getTitle(),
                request.getMetaDescription(),
                request.getH1Tags(),
                request.getH2Tags(),
                request.getTotalImages(),
                request.getMissingAltCount(),
                request.getPageSpeedDesktop(),
                request.getPageSpeedMobile(),
                request.isMobileFriendly(),
                request.isSslEnabled(),
                request.isHasCanonical()
        );

        try {
//            String aiResponse = ollamaChatClient.call(prompt)
//                    .getResult()
//                    .getOutput()
//                    .getContent();

            String aiResponse = ollamaChatClient.call(prompt);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiResponse, SEOAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
