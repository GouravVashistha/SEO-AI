package com.seo_analyzer_service.entity;

import lombok.Data;

import java.util.List;

@Data
public class SEOAnalysisDetails {
    private TitleTag title_tag;
    private MetaDescription meta_description;
    private H1Tag h1_tag;
    private Images images;
    private PageSpeed page_speed;
    private MobileFriendly mobile_friendly;
    private SSLEnabled ssl_enabled;
    private BrokenLinks broken_links;
    private CanonicalTag canonical_tag;
    private CoreWebVitals core_web_vitals;
}