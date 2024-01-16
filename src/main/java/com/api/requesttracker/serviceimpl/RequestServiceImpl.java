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
    @Override
    public Requests createRequests(Long assignToId,String description,MultipartFile file,MultipartFile imageFile,Requests.Priority priority,
                                                             Requests.Status status,String title,String videoLink) {
        Requests request = new Requests();
        UserDetailsImpl userDetail = userSession.getUserDetails();
        setRequestAttributes(request, userDetail, assignToId, description, priority, status, title, videoLink);
        setFileAttributes(request, file);
        setImageAttributes(request, imageFile);
        return requestRepository.save(request);
    }
    private void setRequestAttributes(Requests request,UserDetailsImpl userDetail,Long assignToId,String description,Requests.Priority priority,
                                        Requests.Status status,String title,String videoLink) {
        request.setRequesterID(userDetail.getId());
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setAssignedToID(assignToId);
        request.setTitle(title);
        request.setDescription(description);
        request.setStatus(status);
        request.setPriority(priority);
        request.setVideoLink(videoLink);
    }
    private void setFileAttributes(Requests request, MultipartFile file) {
        try {
            request.setFile(file.getBytes());
            setDocumentAttributes(request, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setImageAttributes(Requests request, MultipartFile imageFile) {
        try {
            request.setImageFile(imageFile.getBytes());
            setImageDocumentAttributes(request, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setDocumentAttributes(Requests request, MultipartFile file) {
        String documentType = file.getContentType();
        MediaType mediaType = MediaType.parseMediaType(documentType);
        String subType = mediaType.getSubtype();
        long documentSize = file.getSize();
        String originalDocumentName = file.getOriginalFilename();
        request.setFileSize(documentSize);
        request.setFilename(originalDocumentName);
        request.setFiletype(subType);
    }
    private void setImageDocumentAttributes(Requests request, MultipartFile imageFile) {
        String fileType = imageFile.getContentType();
        MediaType imageType = MediaType.parseMediaType(fileType);
        String imageSubType = imageType.getSubtype();
        long imageSize = imageFile.getSize();
        String originalImageName = imageFile.getOriginalFilename();
        request.setImageName(originalImageName);
        request.setImageSize(imageSize);
        request.setImageType(imageSubType);
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

}
