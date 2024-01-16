package com.api.requesttracker.dto;

import lombok.Data;

@Data
public class RequestAnalysisCountDTO {
    public int totalNumberOfRequest;
    public int openIssues;
    public int closedIssues;
    public int inProgressissues;
    public int resolvedIssues;


}
