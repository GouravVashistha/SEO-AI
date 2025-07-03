package com.seo_analyzer_service.entity;

import lombok.Data;

import java.util.List;

@Data
public class BrokenLinks {
    private int count;
    private List<String> links;
    private String suggestion;
}