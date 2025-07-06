package com.seo_analyzer_service.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SEOAnalysisRequest {
    private String url;

    private String title;
    private String metaDescription;
    private List<String> h1Tags;
    private List<String> h2Tags;

    private List<String> h1TagsHtml; // ✅ NEW
    private List<String> h2TagsHtml; // ✅ NEW

    private List<String> imageAlts;
    private List<String> missingAltImagesHtml; // ✅ NEW
    private int totalImages;
    private int missingAltCount;

    private String pageSpeedDesktop;
    private String pageSpeedMobile;
    private boolean mobileFriendly;
    private boolean sslEnabled;

    private boolean hasCanonical;
    private String canonicalLink; // ✅ NEW

    private List<String> brokenLinks;

    private Map<String, Object> customParams;
}
