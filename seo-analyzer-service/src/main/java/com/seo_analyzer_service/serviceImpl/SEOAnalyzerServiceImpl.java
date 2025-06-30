package com.seo_analyzer_service.serviceImpl;

import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
import com.seo_analyzer_service.entity.SEOAnalysisRequest;


import com.seo_analyzer_service.serive.SEOAnalyzerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SEOAnalyzerServiceImpl implements SEOAnalyzerService {


    private final RestTemplate restTemplate;

    public SEOAnalyzerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public SEOAnalysisResponse analyze(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        String title = doc.title();
        String metaDescription = doc.select("meta[name=description]").attr("content");
        List<String> h1Tags = doc.select("h1").eachText();
        List<String> h2Tags = doc.select("h2").eachText();
        List<String> headings = doc.select("h1, h2, h3").eachText();

        Elements images = doc.select("img");
        int totalImages = images.size();
        List<String> imageAlts = images.stream()
                .map(img -> img.attr("alt"))
                .collect(Collectors.toList());
        long missingAltCount = imageAlts.stream().filter(String::isEmpty).count();

        // Stubbed for now — can later integrate PageSpeed Insights API
        String pageSpeedDesktop = "3.1s";
        String pageSpeedMobile = "5.5s";

        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
        boolean sslEnabled = url.startsWith("https");
        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();

        List<String> brokenLinks = new ArrayList<>(); // Future: crawl and detect broken links

        // Prepare request object
        SEOAnalysisRequest request = new SEOAnalysisRequest();
        request.setUrl(url);
        request.setTitle(title);
        request.setMetaDescription(metaDescription);
        request.setH1Tags(h1Tags);
        request.setH2Tags(h2Tags);
        request.setHeadings(headings);
        request.setImageAlts(imageAlts);
        request.setTotalImages(totalImages);
        request.setMissingAltCount((int) missingAltCount);
        request.setPageSpeedDesktop(pageSpeedDesktop);
        request.setPageSpeedMobile(pageSpeedMobile);
        request.setMobileFriendly(mobileFriendly);
        request.setSslEnabled(sslEnabled);
        request.setHasCanonical(hasCanonical);
        request.setBrokenLinks(brokenLinks);

        // Call AI Service
        ResponseEntity<SEOAnalysisResponse> response = restTemplate.postForEntity(
                "http://localhost:9091/api/ai/analyze", // ✅ Corrected port (AI service)
                request,
                SEOAnalysisResponse.class
        );

        return response.getBody();
    }
}
