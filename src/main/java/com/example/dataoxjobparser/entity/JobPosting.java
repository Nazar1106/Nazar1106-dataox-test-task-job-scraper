package com.example.dataoxjobparser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class JobPosting {
    private static final int ONE_THOUSAND = 1000;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String positionName;
    private String jobPageUrl;
    private String organizationUrl;
    private String logoUrl;
    private String organizationTitle;
    private String laborFunction;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = ONE_THOUSAND)
    private List<String> location;
    private LocalDate postedDate;
    @Lob
    private String descriptionHtml;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "tag", length = ONE_THOUSAND)
    private List<String> tag;
}
