package com.example.dataoxjobparser.render;

import com.example.dataoxjobparser.entity.JobPosting;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HtmlRenderer {

    private static final int TIMEOUT_MS = 45000;

    public List<JobPosting> enrichAllJobs(List<JobPosting> jobs) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Set<String> seenUrls = ConcurrentHashMap.newKeySet();
        List<CompletableFuture<JobPosting>> futures = new ArrayList<>();

        for (JobPosting job : jobs) {
            if (!seenUrls.add(job.getJobPageUrl())){
                continue;
            }
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Document doc = Jsoup.connect(job.getJobPageUrl())
                            .timeout(TIMEOUT_MS)
                            .userAgent("Mozilla/5.0")
                            .header("Connection", "keep-alive")
                            .get();

                    String description = extractJobDescription(doc);
                    job.setDescriptionHtml(description != null ? description : "Description not found");

                } catch (Exception e) {
                    job.setDescriptionHtml("Error loading description.");
                    job.setTag(Collections.emptyList());
                }
                return job;
            }, executor));
        }
        CompletableFuture<?>[] futuresArray = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(futuresArray).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private String extractJobDescription(Document doc) {
        String[] selectors = {
                "[data-testid*=description]",
                "[class*=description]",
                "[class*=job-description]",
                "[class*=desc]",
                "[id*=description]",
                "article"
        };

        for (String selector : selectors) {
            Elements elements = doc.select(selector);
            for (Element el : elements) {
                String html = el.html().trim();
                if (html.length() > 200) {
                    return html;
                }
            }
        }
        return "Description not found";
    }
}