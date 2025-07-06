package com.seo_analyzer_service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
import com.seo_analyzer_service.config.SSLUtil;
import com.seo_analyzer_service.serive.SEOAnalyzerService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SEOAnalyzerServiceImpl implements SEOAnalyzerService {

    private final OllamaChatClient ollamaChatClient;

    public SEOAnalyzerServiceImpl(OllamaChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    @Override
    public SEOAnalysisResponse analyze(String url) throws IOException {
        // Disable SSL cert validation (for sites with invalid certs)
        SSLUtil.disableSSLCertificateChecking();

        Document doc = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        String title = doc.title();
        String metaDescription = doc.select("meta[name=description]").attr("content");

        Elements h1Elements = doc.select("h1");
        List<String> h1Tags = h1Elements.eachText();
        List<String> h1Html = h1Elements.stream().map(Element::outerHtml).collect(Collectors.toList());

        Elements h2Elements = doc.select("h2");
        List<String> h2Tags = h2Elements.eachText();
        List<String> h2Html = h2Elements.stream().map(Element::outerHtml).collect(Collectors.toList());

        Elements images = doc.select("img");
        int totalImages = images.size();
        List<String> imagesWithoutAlt = new ArrayList<>();

        for (Element img : images) {
            String alt = img.attr("alt");
            if (alt.isEmpty()) {
                imagesWithoutAlt.add(img.outerHtml());
            }
        }

        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
        boolean sslEnabled = url.startsWith("https");
        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();
        List<String> brokenLinks = new ArrayList<>(); // Placeholder for link checker

        String pageSpeedDesktop = "3.1s";
        String pageSpeedMobile = "5.5s";

        // Construct full prompt including all requested fields
        String prompt = """
        Analyze the SEO performance of this webpage and return only a valid JSON in the following format:

        {
          "url": "%s",
          "SEO Score": <0-100>,
          "seo_analysis": {
            "title": "%s",
            "meta_description": "%s",
            "title_tag": {
              "status": "...",
              "length": %d,
              "keyword_found": true,
              "suggestion": "..."
            },
            "meta_description_analysis": {
              "status": "...",
              "length": %d,
              "suggestion": "..."
            },
            "h1_tag": {
              "status": "...",
              "count": %d,
              "tags": %s,
              "html": %s,
              "suggestion": "..."
            },
            "h2_tag": {
              "status": "...",
              "count": %d,
              "tags": %s,
              "html": %s,
              "suggestion": "..."
            },
            "images": {
              "total": %d,
              "missing_alt": %d,
              "missing_images": %s,
              "suggestion": "..."
            },
            "page_speed": {
              "desktop": "%s",
              "mobile": "%s",
              "suggestion": "..."
            },
            "mobile_friendly": {
              "status": %s,
              "suggestion": "..."
            },
            "ssl_enabled": {
              "status": %s,
              "suggestion": "..."
            },
            "broken_links": {
              "count": %d,
              "links": %s,
              "suggestion": "..."
            },
            "canonical_tag": {
              "status": "%s",
              "suggestion": "..."
            },
            "core_web_vitals": {
              "LCP": "3.4s",
              "CLS": "0.12",
              "FID": "25ms",
              "suggestion": "..."
            }
          }
        }

        Notes:
        - Return only the JSON above.
        - Include every field as shown.
        - Do NOT write explanations or extra text outside the JSON.
        """.formatted(
                url,
                title,
                metaDescription,
                title.length(),
                metaDescription.length(),
                h1Tags.size(),
                h1Tags.toString(),
                h1Html.toString(),
                h2Tags.size(),
                h2Tags.toString(),
                h2Html.toString(),
                totalImages,
                imagesWithoutAlt.size(),
                imagesWithoutAlt.toString(),
                pageSpeedDesktop,
                pageSpeedMobile,
                mobileFriendly,
                sslEnabled,
                brokenLinks.size(),
                brokenLinks.toString(),
                hasCanonical ? "Present" : "Missing"
        );

        try {
            String aiResponse = ollamaChatClient.call(prompt);
            System.out.println("AI Response:\n" + aiResponse);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiResponse, SEOAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze SEO for URL: " + url, e);
        }
    }
}