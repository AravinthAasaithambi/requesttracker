package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.services.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportsServiceImpl implements ReportsService {

    public final RequestRepository requestRepository;

    public final UserRepository userRepository;

    @Autowired
    private UserSession userSession;
    /*This is Constructor*/
    public ReportsServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;

    }
    @Override
    public RequestAnalysisCountDTO getAnnalysisReport(){
        RequestAnalysisCountDTO count =new RequestAnalysisCountDTO();
        List<Requests> response=requestRepository.findAll();
        count.setTotalNumberOfRequest(response.size());
        count.setOpenIssues((int) response.stream().filter(a -> Requests.Status.Open.equals(a.getStatus())).count());
        count.setInProgressissues((int) response.stream().filter(a -> Requests.Status.InProgress.equals(a.getStatus())).count());
        count.setResolvedIssues((int) response.stream().filter(a -> Requests.Status.Resolved.equals(a.getStatus())).count());
        count.setClosedIssues((int) response.stream().filter(a -> Requests.Status.Closed.equals(a.getStatus())).count());
        return count;
    }
    @Override
    public RequestAnalysisCountDTO getAnnalysisReportDateRange(LocalDate startDate, LocalDate endDate) {
        RequestAnalysisCountDTO count = new RequestAnalysisCountDTO();
        try {
            List<Requests> response = requestRepository.findAll();
            List<Requests> filteredRequests = response.stream().filter(a -> a.getUpdatedAt().toLocalDate().isAfter(startDate) && a.getUpdatedAt().toLocalDate().isBefore(endDate) || a.getUpdatedAt().toLocalDate().equals(endDate)).collect(Collectors.toList());
            count = getCount(filteredRequests);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    public RequestAnalysisCountDTO getCount(List<Requests> filteredRequests){
        RequestAnalysisCountDTO count = new RequestAnalysisCountDTO();
        count.setTotalNumberOfRequest(filteredRequests.size());
        count.setOpenIssues((int) filteredRequests.stream().filter(a -> Requests.Status.Open.equals(a.getStatus())).count());
        count.setInProgressissues((int) filteredRequests.stream().filter(a -> Requests.Status.InProgress.equals(a.getStatus())).count());
        count.setResolvedIssues((int) filteredRequests.stream().filter(a -> Requests.Status.Resolved.equals(a.getStatus())).count());
        count.setClosedIssues((int) filteredRequests.stream().filter(a -> Requests.Status.Closed.equals(a.getStatus())).count());
        return count;

    }
}
