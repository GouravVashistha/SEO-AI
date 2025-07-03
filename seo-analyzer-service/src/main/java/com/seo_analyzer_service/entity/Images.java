package com.seo_analyzer_service.entity;

import lombok.Data;

@Data
public class Images {
    private int total;
    private int missing_alt;
    private String suggestion;
}
