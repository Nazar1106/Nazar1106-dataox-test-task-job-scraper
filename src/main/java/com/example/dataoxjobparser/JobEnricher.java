package com.example.dataoxjobparser;

import com.example.dataoxjobparser.entity.JobPosting;

public interface JobEnricher {
    boolean supports(String url);
    JobPosting enrich(JobPosting job);
}
