package com.seo_analyzer_service.controller;

import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
import com.seo_analyzer_service.serive.SEOAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// controller for SEO analysis service
@RestController
@RequestMapping("/api/seo")
public class SEOAnalyzerController {

    private final SEOAnalyzerService service;

    public SEOAnalyzerController(SEOAnalyzerService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public ResponseEntity<SEOAnalysisResponse> analyze(@RequestParam String url) {
        try {
            SEOAnalysisResponse result = service.analyze(url);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
