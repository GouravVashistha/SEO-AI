//package com.seo_analyzer_service.serviceImpl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.seo_analyzer_service.entity.SEOAnalysisRequest;
//import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
//import com.seo_analyzer_service.serive.SEOAnalyzerService;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.springframework.ai.ollama.OllamaChatClient;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class SEOAnalyzerServiceImpl implements SEOAnalyzerService {
//
//    private final OllamaChatClient ollamaChatClient;
//
//    public SEOAnalyzerServiceImpl(OllamaChatClient ollamaChatClient) {
//        this.ollamaChatClient = ollamaChatClient;
//    }
//
//    @Override
//    public SEOAnalysisResponse analyze(String url) throws IOException {
//        Document doc = Jsoup.connect(url).get();
//
//        String title = doc.title();
//        String metaDescription = doc.select("meta[name=description]").attr("content");
//        List<String> h1Tags = doc.select("h1").eachText();
//        List<String> h2Tags = doc.select("h2").eachText();
//        List<String> headings = doc.select("h1, h2, h3").eachText();
//
//        Elements images = doc.select("img");
//        int totalImages = images.size();
//        List<String> imageAlts = images.stream().map(img -> img.attr("alt")).collect(Collectors.toList());
//        long missingAltCount = imageAlts.stream().filter(String::isEmpty).count();
//
//        String pageSpeedDesktop = "3.1s";
//        String pageSpeedMobile = "5.5s";
//        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
//        boolean sslEnabled = url.startsWith("https");
//        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();
//        List<String> brokenLinks = new ArrayList<>();
//
//        // Build prompt for AI
//        String prompt = """
//        Analyze the SEO performance of this webpage based on the following raw data:
//
//        Title: %s
//        Meta Description: %s
//        H1 Tags: %s
//        H2 Tags: %s
//        Total Images: %d
//        Missing ALT tags: %d
//        Page Speed (Desktop): %s
//        Page Speed (Mobile): %s
//        Mobile Friendly: %s
//        SSL Enabled: %s
//        Has Canonical: %s
//
//        Return only a valid JSON response with SEO score and suggestions.
//        """.formatted(
//                title,
//                metaDescription,
//                h1Tags,
//                h2Tags,
//                totalImages,
//                missingAltCount,
//                pageSpeedDesktop,
//                pageSpeedMobile,
//                mobileFriendly,
//                sslEnabled,
//                hasCanonical
//        );
//
//        try {
//            String aiResponse = ollamaChatClient.call(prompt);
//            System.out.println("Ollama AI Response: " + aiResponse);
//            ObjectMapper mapper = new ObjectMapper();
//            System.out.println(" mapper line fail");
//            SEOAnalysisResponse response = mapper.readValue(aiResponse, SEOAnalysisResponse.class);
//            System.out.println(" responce object line fail");
//            response.setUrl(url); // set original URL in response
//            System.out.println("Response object created successfully: " + response);
//            return response;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse AI response", e);
//        }
//    }
//}
//=============================================================================================
//
//package com.seo_analyzer_service.serviceImpl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
//import com.seo_analyzer_service.serive.SEOAnalyzerService;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.springframework.ai.ollama.OllamaChatClient;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class SEOAnalyzerServiceImpl implements SEOAnalyzerService {
//
//    private final OllamaChatClient ollamaChatClient;
//
//    public SEOAnalyzerServiceImpl(OllamaChatClient ollamaChatClient) {
//        this.ollamaChatClient = ollamaChatClient;
//    }
//
//    @Override
//    public SEOAnalysisResponse analyze(String url) throws IOException {
//        Document doc = Jsoup.connect(url).get();
//
//        String title = doc.title();
//        String metaDescription = doc.select("meta[name=description]").attr("content");
//        List<String> h1Tags = doc.select("h1").eachText();
//        List<String> h2Tags = doc.select("h2").eachText();
//        Elements images = doc.select("img");
//
//        int totalImages = images.size();
//        List<String> imageAlts = images.stream().map(img -> img.attr("alt")).collect(Collectors.toList());
//        long missingAltCount = imageAlts.stream().filter(String::isEmpty).count();
//
//        String pageSpeedDesktop = "3.1s";
//        String pageSpeedMobile = "5.5s";
//        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
//        boolean sslEnabled = url.startsWith("https");
//        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();
//        List<String> brokenLinks = new ArrayList<>();
//
//        String prompt = """
//        Return only a valid JSON with this exact structure:
//
//        {
//          "url": "%s",
//          "seo_analysis": {
//            "title_tag": {
//              "status": "Too Long",
//              "length": 78,
//              "keyword_found": true,
//              "suggestion": "Shorten the title to under 60 characters for better display in search results."
//            },
//            "meta_description": {
//              "status": "Missing",
//              "suggestion": "Add a meta description with relevant keywords to improve click-through rates."
//            },
//            "h1_tag": {
//              "status": "Multiple H1s",
//              "count": %d,
//              "suggestion": "Use only one H1 tag per page to maintain proper content hierarchy."
//            },
//            "images": {
//              "total": %d,
//              "missing_alt": %d,
//              "suggestion": "Add descriptive ALT text to all images to improve accessibility and SEO."
//            },
//            "page_speed": {
//              "desktop": "%s",
//              "mobile": "%s",
//              "suggestion": "Optimize images and reduce script load to improve mobile page speed."
//            },
//            "mobile_friendly": {
//              "status": %s,
//              "suggestion": "Use responsive design or media queries to make the website mobile-friendly."
//            },
//            "ssl_enabled": {
//              "status": %s,
//              "suggestion": "Install an SSL certificate to secure the site and improve trust."
//            },
//            "broken_links": {
//              "count": %d,
//              "links": [],
//              "suggestion": "Fix or remove broken links to enhance user experience and SEO crawlability."
//            },
//            "canonical_tag": {
//              "status": "%s",
//              "suggestion": "Add a canonical tag to avoid duplicate content issues."
//            },
//            "core_web_vitals": {
//              "LCP": "3.4s",
//              "CLS": "0.12",
//              "FID": "25ms",
//              "suggestion": "Improve LCP by optimizing large images or elements loading above the fold."
//            }
//          }
//        }
//
//        Respond only with valid JSON.
//        """.formatted(
//                url,
//                h1Tags.size(),
//                totalImages,
//                missingAltCount,
//                pageSpeedDesktop,
//                pageSpeedMobile,
//                mobileFriendly,
//                sslEnabled,
//                brokenLinks.size(),
//                hasCanonical ? "Present" : "Missing"
//        );
//
//        try {
//            String aiResponse = ollamaChatClient.call(prompt);
//            System.out.println("AI Response: " + aiResponse);
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(aiResponse, SEOAnalysisResponse.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse AI response", e);
//        }
//    }
//}


/*
package com.seo_analyzer_service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seo_analyzer_service.Responce.SEOAnalysisResponse;
import com.seo_analyzer_service.serive.SEOAnalyzerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        Document doc = Jsoup.connect(url).get();

        String title = doc.title();
        String metaDescription = doc.select("meta[name=description]").attr("content");
        List<String> h1Tags = doc.select("h1").eachText();
        List<String> h2Tags = doc.select("h2").eachText();
        Elements images = doc.select("img");

        int totalImages = images.size();
        List<String> imageAlts = images.stream().map(img -> img.attr("alt")).collect(Collectors.toList());
        long missingAltCount = imageAlts.stream().filter(String::isEmpty).count();

        String pageSpeedDesktop = "3.1s";
        String pageSpeedMobile = "5.5s";
        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
        boolean sslEnabled = url.startsWith("https");
        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();
        List<String> brokenLinks = new ArrayList<>();

        //  NEW PROMPT to let AI decide score
        String prompt = """
        Analyze the SEO performance of this webpage based on the following data and return only a valid JSON object with this format:
        
        {
          "url": "<url>",
          "SEO Score": <score from 0 to 100>,
          "seo_analysis": {
            "title_tag": {
              "status": "...",
              "length": <int>,
              "keyword_found": <true|false>,
              "suggestion": "..."
            },
            "meta_description": {
              "status": "...",
              "suggestion": "..."
            },
            "h1_tag": {
              "status": "...",
              "count": <int>,
              "suggestion": "..."
            },
            "images": {
              "total": <int>,
              "missing_alt": <int>,
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
              "links": [],
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

        Your task:
        - Fill all fields accurately using SEO knowledge
        - Assign a SEO Score (0–100) based on how many best practices are met
        - Return valid JSON only
        - Do NOT include explanations, only the raw JSON
        - JSON must match format exactly

        Use this input:

        URL: %s
        Title: %s
        Meta Description: %s
        H1 Tags: %s
        H2 Tags: %s
        Total Images: %d
        Missing ALT Tags: %d
        Mobile Friendly: %s
        SSL Enabled: %s
        Canonical Tag Present: %s
        Broken Links Count: %d
        """.formatted(
                pageSpeedDesktop,
                pageSpeedMobile,
                mobileFriendly,
                sslEnabled,
                brokenLinks.size(),
                hasCanonical ? "Present" : "Missing",
                url,
                title,
                metaDescription,
                h1Tags,
                h2Tags,
                totalImages,
                missingAltCount,
                mobileFriendly,
                sslEnabled,
                hasCanonical ? "Present" : "Missing",
                brokenLinks.size()
        );

        try {
            String aiResponse = ollamaChatClient.call(prompt);
            System.out.println("AI Response:\n" + aiResponse);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiResponse, SEOAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
//http://localhost:9090/api/seo/analyze?url=https://www.amitsharma.online/



 */
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

//@Service
//public class SEOAnalyzerServiceImpl implements SEOAnalyzerService {
//
//    private final OllamaChatClient ollamaChatClient;
//
//    public SEOAnalyzerServiceImpl(OllamaChatClient ollamaChatClient) {
//        this.ollamaChatClient = ollamaChatClient;
//    }
//
//    @Override
//    public SEOAnalysisResponse analyze(String url) throws IOException {
//        // ✅ Disable SSL certificate validation (handles expired/self-signed certs)
//        SSLUtil.disableSSLCertificateChecking();
//
//        Document doc = Jsoup.connect(url)
//                .userAgent("Mozilla/5.0")
//                .timeout(10000)
//                .get();
//
//        // Extract SEO data
//        String title = doc.title();
//        String metaDescription = doc.select("meta[name=description]").attr("content");
//
//        Elements h1Elements = doc.select("h1");
//        List<String> h1Tags = h1Elements.eachText();
//        List<String> h1Html = h1Elements.stream().map(Element::outerHtml).collect(Collectors.toList());
//
//        Elements h2Elements = doc.select("h2");
//        List<String> h2Tags = h2Elements.eachText();
//        List<String> h2Html = h2Elements.stream().map(Element::outerHtml).collect(Collectors.toList());
//
//        Elements images = doc.select("img");
//        int totalImages = images.size();
//
//        List<String> imageAlts = new ArrayList<>();
//        List<String> imagesWithoutAlt = new ArrayList<>();
//
//        for (Element img : images) {
//            String alt = img.attr("alt");
//            imageAlts.add(alt);
//            if (alt.isEmpty()) {
//                imagesWithoutAlt.add(img.outerHtml());
//            }
//        }
//
//        boolean mobileFriendly = doc.select("meta[name=viewport]").size() > 0;
//        boolean sslEnabled = url.startsWith("https");
//        boolean hasCanonical = !doc.select("link[rel=canonical]").isEmpty();
//
//        List<String> brokenLinks = new ArrayList<>(); // Can add link checker later
//
//        // Hardcoded Page Speed & Core Web Vitals (replace with real later)
//        String pageSpeedDesktop = "3.1s";
//        String pageSpeedMobile = "5.5s";
//
//        // Build the AI prompt
//        String prompt = """
//        Analyze the SEO performance of this webpage based on the following data and return only a valid JSON object with this format:
//
//        {
//          "url": "<url>",
//          "SEO Score": <score from 0 to 100>,
//          "seo_analysis": {
//            "title_tag": {
//              "status": "...",
//              "length": <int>,
//              "keyword_found": <true|false>,
//              "suggestion": "..."
//            },
//            "meta_description": {
//              "status": "...",
//              "suggestion": "..."
//            },
//            "h1_tag": {
//              "status": "...",
//              "count": <int>,
//              "html": %s,
//              "suggestion": "..."
//            },
//            "images": {
//              "total": %d,
//              "missing_alt": %d,
//              "missing_images": %s,
//              "suggestion": "..."
//            },
//            "page_speed": {
//              "desktop": "%s",
//              "mobile": "%s",
//              "suggestion": "..."
//            },
//            "mobile_friendly": {
//              "status": %s,
//              "suggestion": "..."
//            },
//            "ssl_enabled": {
//              "status": %s,
//              "suggestion": "..."
//            },
//            "broken_links": {
//              "count": %d,
//              "links": %s,
//              "suggestion": "..."
//            },
//            "canonical_tag": {
//              "status": "%s",
//              "suggestion": "..."
//            },
//            "core_web_vitals": {
//              "LCP": "3.4s",
//              "CLS": "0.12",
//              "FID": "25ms",
//              "suggestion": "..."
//            }
//          }
//        }
//
//        Your task:
//        - Fill all fields accurately using SEO knowledge
//        - Assign a SEO Score (0–100) based on how many best practices are met
//        - Return valid JSON only
//        - Do NOT include explanations, only the raw JSON
//        - JSON must match format exactly
//
//        Use this input:
//
//        URL: %s
//        Title: %s
//        Meta Description: %s
//        H1 Tags: %s
//        H2 Tags: %s
//        Total Images: %d
//        Missing ALT Tags: %d
//        Mobile Friendly: %s
//        SSL Enabled: %s
//        Canonical Tag Present: %s
//        Broken Links Count: %d
//        """.formatted(
//                h1Html.toString(),
//                totalImages,
//                imagesWithoutAlt.size(),
//                imagesWithoutAlt.toString(),
//                pageSpeedDesktop,
//                pageSpeedMobile,
//                mobileFriendly,
//                sslEnabled,
//                brokenLinks.size(),
//                brokenLinks.toString(),
//                hasCanonical ? "Present" : "Missing",
//                url,
//                title,
//                metaDescription,
//                h1Tags,
//                h2Tags,
//                totalImages,
//                imagesWithoutAlt.size(),
//                mobileFriendly,
//                sslEnabled,
//                hasCanonical ? "Present" : "Missing",
//                brokenLinks.size()
//        );
//
//        try {
//            String aiResponse = ollamaChatClient.call(prompt);
//            System.out.println("AI Response:\n" + aiResponse);
//
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(aiResponse, SEOAnalysisResponse.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to analyze SEO for URL: " + url, e);
//        }
//    }
//}


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