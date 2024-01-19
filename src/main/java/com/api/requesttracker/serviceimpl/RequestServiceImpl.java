package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.dto.RequestAnalysisCountDTO;
import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*This is the Implementation File where all the implementation on done here for the Methods specified in Request Service*/
@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    /*Invoking the properties of a Request Repository File*/
    public final RequestRepository requestRepository;

    public final UserRepository userRepository;

    @Autowired
    private UserSession userSession;
    /*This is Constructor*/
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;

            }

    /*This method specifies to find one single Request based on the request id given*/
    @Override
    public Optional<Requests> findById(Long id) {
        Optional<Requests> response = requestRepository.findById(id);
        return response;
    }
    @Override
    public Requests getRequestById(Long requestid){
        Requests request = requestRepository.getRequestById(requestid);
        return request;
    }
    /*This method specifies to find all Request*/
    @Override
    public List<Requests> getAllRequest() {
        List<Requests> response = requestRepository.findAll();
        return response;
    }

    /*This method specifies to Update one single Request*/
    @Override
    public Requests updateRequest(Requests request){
        Requests response=new Requests();
        response.setRequestID(request.getRequestID());
        response.setRequesterID(request.getRequesterID());
        response.setDescription(request.getDescription());
        response.setUpdatedAt(request.getUpdatedAt());
        response.setStatus(request.getStatus());
        response.setPriority(request.getPriority());
        response.setTitle(request.getTitle());
        response.setAssignedToID(request.getAssignedToID());
        return requestRepository.save(response);
    }
    @Override
    public String deleteRequest(Long id){
        requestRepository.deleteById(id);
        return "Request Deleted Successfully";
    }





}
