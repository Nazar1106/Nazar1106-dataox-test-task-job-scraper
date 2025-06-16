package com.example.dataoxjobparser.service;

import com.example.dataoxjobparser.dto.JobSearchParametersDto;
import com.example.dataoxjobparser.entity.JobPosting;
import java.util.List;

public interface JobScraperService {
    List<JobPosting> scrapeJobsByFunction(String jobFunction);
    List<JobPosting> filterAndSort(JobSearchParametersDto params);
}
