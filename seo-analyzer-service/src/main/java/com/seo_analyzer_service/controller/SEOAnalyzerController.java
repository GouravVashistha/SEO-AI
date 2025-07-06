//package com.seo_analyzer_service.controller;
//
//import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
//import com.seo_analyzer_service.serive.SEOAnalyzerService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api/seo")
//public class SEOAnalyzerController {
//
//    private final SEOAnalyzerService service;
//
//    public SEOAnalyzerController(SEOAnalyzerService service) {
//        this.service = service;
//    }
//
//    @PostMapping("/analyze")
//    public ResponseEntity<SEOAnalysisResponse> analyze(@RequestParam String url) {
//        try {
//            SEOAnalysisResponse result = service.analyze(url);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//}


package com.seo_analyzer_service.controller;

import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
import com.seo_analyzer_service.serive.SEOAnalyzerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/seo")
public class SEOAnalyzerController {

    private final SEOAnalyzerService service;

    public SEOAnalyzerController(SEOAnalyzerService service) {
        this.service = service;
    }

    // ✅ Use GET for easy testing in browser/Postman
    @GetMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestParam String url) {
        try {
            SEOAnalysisResponse result = service.analyze(url);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // ✅ Log full error for debugging
            e.printStackTrace();

            // ✅ Return readable error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("Failed to analyze SEO for URL: " + url, e.getMessage())
            );
        }
    }

    // ✅ Simple error response DTO
    static class ErrorResponse {
        private final String message;
        private final String error;

        public ErrorResponse(String message, String error) {
            this.message = message;
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public String getError() {
            return error;
        }
    }
}
