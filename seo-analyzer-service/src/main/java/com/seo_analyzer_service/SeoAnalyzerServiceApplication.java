package com.seo_analyzer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
public class SeoAnalyzerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeoAnalyzerServiceApplication.class, args);
	}

}
