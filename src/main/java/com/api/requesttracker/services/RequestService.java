package com.api.requesttracker.services;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import com.api.requesttracker.entity.Requests;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
/*This is a Service class Which handles all the request on the Request Table Items*/
public interface RequestService  {
    /*This method specifies to find one single Request based on the request id given*/
    Optional<Requests> findById(Long id);

    Requests getRequestById(Long requestid);

    /*This method specifies to find all Request*/
    List<Requests> getAllRequest();

    /*This method specifies to Update one single Request*/
    Requests updateRequest(Requests request);

    String deleteRequest(Long id);

}


