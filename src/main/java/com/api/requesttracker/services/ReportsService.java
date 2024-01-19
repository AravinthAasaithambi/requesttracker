package com.api.requesttracker.services;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ReportsService {
    RequestAnalysisCountDTO getAnnalysisReport();

    RequestAnalysisCountDTO getAnnalysisReportDateRange(LocalDate startDate, LocalDate endDate);
}
