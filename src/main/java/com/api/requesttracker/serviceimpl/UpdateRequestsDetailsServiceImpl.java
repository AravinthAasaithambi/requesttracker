package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.services.UpdateRequestsDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UpdateRequestsDetailsServiceImpl implements UpdateRequestsDetailsService {
    /*Invoking the properties of a Request Repository File*/
    public final RequestRepository requestRepository;

    public final UserRepository userRepository;

    @Autowired
    private UserSession userSession;
    /*This is Constructor*/
    public UpdateRequestsDetailsServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;

    }

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
    @Override
    public Requests rejectRequest(Long requestId) {
        try {
            Optional<Requests> optionalRequest = findById(requestId);
            UserDetailsImpl userDetail = getUserDetails();
            if (optionalRequest.isPresent()) {
                Requests request = optionalRequest.get();
                updateRequestOnRejection(request, userDetail);
                Requests updatedRequest = saveUpdatedRequest(request);
                return updatedRequest;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private UserDetailsImpl getUserDetails() {
        return userSession.getUserDetails();
    }
    private void updateRequestOnRejection(Requests request, UserDetailsImpl userDetail) {
        if (!request.isRejected()) {
            request.setRejected(true);
            request.setAssignedToID(null);
            Long rejectedByUserId = userDetail.getId();
            User rejectedByUser = userRepository.findById(rejectedByUserId).orElse(null);
            if (rejectedByUser != null) {
                request.setRejectedByUser(rejectedByUser);
            }
        }
    }
    private Requests saveUpdatedRequest(Requests request) {
        return requestRepository.save(request);
    }

    @Override
    public String assignRequest(Long userId, Long requestId) {
        Optional<User> optionalUser = getUserById(userId);
        Optional<Requests> optionalRequest = findById(requestId);
        if (optionalUser.isPresent() && optionalRequest.isPresent()) {
            User user = optionalUser.get();
            Requests request = optionalRequest.get();
            handleRejectedRequest(request);
            if (!userHasRejectedRequest(user, request)) {
                assignRequestToUser(user, request);
                return "Request assigned successfully.";
            } else {
                return "User has previously rejected this request and cannot be assigned.";
            }
        }
        return "User or request not found.";
    }
    private Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    private void handleRejectedRequest(Requests request) {
        if (request.isRejected()) {
            request.setRejected(false);
        }
    }
    private void assignRequestToUser(User user, Requests request) {
        request.setAssignedToID(user.getId());
        requestRepository.save(request);
    }
    private boolean userHasRejectedRequest(User user, Requests request) {
        return request.isRejected() && request.getRejectedByUser() != null && request.getRejectedByUser().equals(user);
    }

    @Override
    public Requests changeStatus(Long id, Requests.Status status){
        Requests request = requestRepository.getRequestById(id);
        request.setRequestID(id);
        request.setStatus(status);
        requestRepository.save(request);
        return request;
    }
    @Override
    public Requests changePriority(Long id, Requests.Priority priority){
        Requests request = requestRepository.getRequestById(id);
        request.setRequestID(id);
        request.setPriority(priority);
        requestRepository.save(request);
        return request;
    }

}
