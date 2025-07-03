package com.seo_analyzer_service.entity;


import lombok.Data;

@Data
public class SSLEnabled {
    private boolean status;
    private String suggestion;
}