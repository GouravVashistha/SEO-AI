package com.seo_ai_service.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SEOAnalysisRequest {
    private String url;

    // On-page SEO
    private String title;
    private String metaDescription;
    private List<String> h1Tags;
    private List<String> h2Tags;
    private List<String> headings;
    private List<String> imageAlts;
    private int totalImages;
    private int missingAltCount;

    // Technical SEO
    private String pageSpeedDesktop;
    private String pageSpeedMobile;
    private boolean mobileFriendly;
    private boolean sslEnabled;
    private boolean hasCanonical;
    private List<String> brokenLinks;

    // Optional
    private Map<String, Object> customParams;
}