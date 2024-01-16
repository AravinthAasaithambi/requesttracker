package com.api.requesttracker.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestAnalysisDTO {
    public Date startDate;
    public Date endDate;
    public String requestCategory;
    public int numberOfRequest;
    public int numberOfRequestResolved;
}
