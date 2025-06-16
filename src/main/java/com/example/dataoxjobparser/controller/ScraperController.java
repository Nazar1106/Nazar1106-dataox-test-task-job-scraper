package com.example.dataoxjobparser.controller;

import com.example.dataoxjobparser.dto.JobSearchParametersDto;
import com.example.dataoxjobparser.entity.JobPosting;
import com.example.dataoxjobparser.service.JobScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scraper")
public class ScraperController {
    private final JobScraperService scraperService;

    public ScraperController(JobScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/jobs")
    public List<JobPosting> getJobsByFunction(@RequestParam String function) {
        return scraperService.scrapeJobsByFunction(function);
    }

    @GetMapping("/filtered")
    public List<JobPosting> filterAndSortJobs(JobSearchParametersDto params) {
        return scraperService.filterAndSort(params);
    }
}
