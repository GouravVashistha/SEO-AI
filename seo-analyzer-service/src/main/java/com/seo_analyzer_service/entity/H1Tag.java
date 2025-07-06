package com.seo_analyzer_service.entity;

import lombok.Data;

import java.util.List;

@Data
public class H1Tag {
    private String status;
    private int count;
    private List<String> htmlTags; // ✅ NEW
    private String suggestion;
}