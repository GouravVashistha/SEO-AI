package com.seo_analyzer_service.entity;

import lombok.Data;

import java.util.List;

@Data
public class Images {
    private int total;
    private int missing_alt;
    private List<String> missingAltTags; // ✅ NEW
    private String suggestion;
}
