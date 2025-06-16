package com.example.dataoxjobparser.dto;

import java.time.LocalDate;
import java.util.List;

public record JobSearchParametersDto (  List<String> locations,
                                        LocalDate startDate,
                                        LocalDate endDate,
                                        String sortBy,
                                        String sortDirection,
                                        List<String> positionNames,
                                        List<String> organizationTitles,
                                        List<String> laborFunctions,
                                        List<String> tags){
}
