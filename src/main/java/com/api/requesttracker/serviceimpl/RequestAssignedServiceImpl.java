package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.services.RequestAssignedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class RequestAssignedServiceImpl implements RequestAssignedService {
    public final RequestRepository requestRepository;

    public final UserRepository userRepository;

    @Autowired
    private UserSession userSession;
    /*This is Constructor*/
    public RequestAssignedServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;

    }


    @Override
    public List<Requests> assingnedRequestForUser(){
        UserDetailsImpl userdetail= userSession.getUserDetails();
        List<Requests> result= requestRepository.findAll();
        var response = result.stream().filter(a->a.getAssignedToID().equals(userdetail.getId())).collect(Collectors.toList());
        return response;
    }
    @Override
    public List<Requests> assingnedRequestByUser(){
        UserDetailsImpl userdetail= userSession.getUserDetails();
        List<Requests> result= requestRepository.findAll();
        var response = result.stream().filter(a->a.getRequesterID() ==  userdetail.getId()).collect(Collectors.toList());
        return response;
    }
}
