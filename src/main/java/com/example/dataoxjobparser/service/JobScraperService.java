package com.example.dataoxjobparser.service;

import com.example.dataoxjobparser.entity.JobPosting;

import java.util.List;

public interface JobScraperService {
    List<JobPosting> scrapeJobsByFunction(String jobFunction);

}
