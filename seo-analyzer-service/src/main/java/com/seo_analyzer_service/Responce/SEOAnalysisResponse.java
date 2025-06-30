package com.seo_analyzer_service.Responce;

import lombok.Data;

import java.util.Map;

@Data
public class SEOAnalysisResponse {
    private String url;
    private int seoScore;
    private Map<String, Object> analysis; // This will include all the suggestions (title, meta, H1, etc.)
}