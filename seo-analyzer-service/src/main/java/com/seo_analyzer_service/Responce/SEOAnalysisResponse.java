package com.seo_analyzer_service.Responce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class SEOAnalysisResponse {
    private String url;

    @JsonProperty("SEO Score")
    private int seoScore;

    @JsonProperty("seo_analysis")
    private Map<String, Object> seoAnalysis;
}