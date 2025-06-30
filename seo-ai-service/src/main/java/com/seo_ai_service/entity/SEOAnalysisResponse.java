package com.seo_ai_service.entity;

import lombok.Data;

import java.util.Map;

@Data
public class SEOAnalysisResponse {
    private String url;
    private int seoScore;

    // Analysis section with title, meta, h1, images, etc. — dynamic in structure
    private Map<String, Object> analysis;
}
