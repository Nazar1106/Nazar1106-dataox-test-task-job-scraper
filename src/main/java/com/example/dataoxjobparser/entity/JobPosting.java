package com.example.dataoxjobparser.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String positionName;
    private String jobPageUrl;
    private String organizationUrl;
    private String logoUrl;
    private String organizationTitle;
    private String laborFunction;
    @ElementCollection
    @Column(length = 1000)
    private List<String> location;
    private LocalDate postedDate;
    @Lob
    private String descriptionHtml;
    @ElementCollection
    @Column(name = "tag", length = 1000)
    private List<String> tag;


    //    @OneToMany(mappedBy = "jobPosting",
//        cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<JobPostingTag> tags = new ArrayList<>();
//    @OneToMany(mappedBy = "jobPosting",
//            cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<JobPostingLocation> locations = new ArrayList<>();
}
