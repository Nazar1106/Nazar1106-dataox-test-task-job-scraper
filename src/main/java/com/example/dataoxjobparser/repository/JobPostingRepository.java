package com.example.dataoxjobparser.repository;

import com.example.dataoxjobparser.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository  extends JpaRepository<JobPosting, Long> { }
