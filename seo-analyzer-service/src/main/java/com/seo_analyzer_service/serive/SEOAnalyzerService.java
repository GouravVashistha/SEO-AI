package com.seo_analyzer_service.serive;

import com.seo_analyzer_service.Responce.SEOAnalysisResponse;

import java.io.IOException;

public interface SEOAnalyzerService {
    SEOAnalysisResponse analyze(String url) throws IOException;
}
