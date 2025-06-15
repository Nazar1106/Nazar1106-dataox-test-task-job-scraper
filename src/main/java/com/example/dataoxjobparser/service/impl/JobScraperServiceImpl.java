package com.example.dataoxjobparser.service.impl;

import com.example.dataoxjobparser.entity.JobPosting;
import com.example.dataoxjobparser.render.HtmlRenderer;
import com.example.dataoxjobparser.repository.JobPostingRepository;
import com.example.dataoxjobparser.service.JobScraperService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class JobScraperServiceImpl implements JobScraperService {

    public static final String PARSE_JOB_DATA_FOR_PAGE = "Failed to fetch or parse job data for page ";
    public static final String JOB_SCRAPING_EXCEEDED_TIME_LIMIT = "Job scraping exceeded time limit";
    public static final String JOB_SCRAPING_WAS_INTERRUPTED = "Job scraping was interrupted";
    public static final String THE_SCRAPING_TASKS_FAILED = "One of the scraping tasks failed";

    private final JobPostingRepository repository;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final HtmlRenderer htmlRenderer = new HtmlRenderer();

    @Transactional
    @Override
    public List<JobPosting> scrapeJobsByFunction(String jobFunction) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<JobPosting> allJobs = new CopyOnWriteArrayList<>();
        int maxPages = 100;

        try {
            List<CompletableFuture<Void>> pageFutures = IntStream.range(0, maxPages)
                    .mapToObj(page -> CompletableFuture.runAsync(() -> {
                try {
                    String jsonRequest = """
                                {
                                  "hitsPerPage": 50,
                                  "page": %d,
                                  "filters": {},
                                  "query": "%s"
                                }
                            """.formatted(page, jobFunction);

                    Request request = new Request.Builder()
                            .url("https://api.getro.com/api/v2/collections/89/search/jobs")
                            .post(RequestBody.create(jsonRequest, MediaType.get("application/json")))
                            .addHeader("Accept", "application/json")
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful() || response.body() == null) return;
                        JsonNode root = mapper.readTree(response.body().string());
                        JsonNode hits = root.path("results").path("jobs");
                        if (hits == null || !hits.elements().hasNext()) return;

                        List<JobPosting> jobsBatch = new ArrayList<>();

                        for (JsonNode hit : hits) {
                            JobPosting job = new JobPosting();
                            JsonNode org = hit.path("organization");

                            job.setPositionName(hit.path("title").asText());
                            job.setJobPageUrl(hit.path("url").asText());

                            LocalDate date = Instant.ofEpochSecond(hit.path("created_at")
                                    .asLong()).atZone(ZoneOffset.UTC)
                                    .toLocalDate();
                            job.setPostedDate(date);
                            job.setOrganizationTitle(org.path("name")
                                    .asText());
                            job.setLogoUrl(org.path("logo_url")
                                    .asText());
                            job.setOrganizationUrl("https://jobs.techstars.com/organizations/"
                                    + org.path("slug")
                                    .asText());

                            String laborFunction = hit.hasNonNull("seniority") ? hit.get("seniority")
                                    .asText() : null;
                            job.setLaborFunction(laborFunction);
                            List<String> locations = new ArrayList<>();
                            hit.path("locations").forEach(loc -> locations.add(loc.asText()));
                            job.setLocation(locations);

                            List<String> tags = new ArrayList<>();
                            org.path("topics").forEach(topic -> tags.add(topic.asText()));
                            org.path("industry_tags").forEach(tag -> tags.add(tag.asText()));
                            job.setTag(tags.stream().distinct().toList());

                            jobsBatch.add(job);
                        }

                        List<List<JobPosting>> batches = Lists.partition(jobsBatch, 20);
                        for (List<JobPosting> batch : batches) {
                            List<JobPosting> enriched = htmlRenderer.enrichAllJobs(batch);
                            repository.saveAll(enriched);
                            allJobs.addAll(enriched);
                        }
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(PARSE_JOB_DATA_FOR_PAGE + page, e);
                }
            }, executor)).toList();

            CompletableFuture.allOf(pageFutures.toArray(new CompletableFuture[0])).get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            throw new IllegalStateException(JOB_SCRAPING_EXCEEDED_TIME_LIMIT, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(JOB_SCRAPING_WAS_INTERRUPTED, e);
        } catch (ExecutionException e) {
            throw new RuntimeException(THE_SCRAPING_TASKS_FAILED, e.getCause());
        } finally {
            executor.shutdown();
        }
        return allJobs;
    }
}
