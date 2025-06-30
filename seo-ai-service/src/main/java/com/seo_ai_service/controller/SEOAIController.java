package com.seo_ai_service.controller;

import com.seo_ai_service.entity.SEOAnalysisRequest;
import com.seo_ai_service.entity.SEOAnalysisResponse;
import com.seo_ai_service.service.SEOAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class SEOAIController {

    private final SEOAIService seoaiService;

    public SEOAIController(SEOAIService seoaiService) {
        this.seoaiService = seoaiService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<SEOAnalysisResponse> analyzeWithAI(@RequestBody SEOAnalysisRequest request) {
        return ResponseEntity.ok(seoaiService.analyze(request));
    }
}