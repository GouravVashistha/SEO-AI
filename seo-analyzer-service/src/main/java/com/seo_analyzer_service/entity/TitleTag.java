package com.seo_analyzer_service.entity;

import lombok.Data;

@Data
public class TitleTag {
    private String status;
    private int length;
    private boolean keyword_found;
    private String suggestion;
}
