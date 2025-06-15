package com.example.dataoxjobparser.controller;

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

    //todo:Additionally, I would like the application to have an option to filter or sort
    // the results based on different criteria, such as job location or posting date.

    @GetMapping("/jobs")
    public List<JobPosting> getJobsByFunction(@RequestParam String function) {
        return scraperService.scrapeJobsByFunction(function);
    }
}
