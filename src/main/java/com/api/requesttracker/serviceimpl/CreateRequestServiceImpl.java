package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.security.services.UserDetailsImpl;
import com.api.requesttracker.security.services.UserSession;
import com.api.requesttracker.services.CreateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional
public class CreateRequestServiceImpl implements CreateRequestService {

    /*Invoking the properties of a Request Repository File*/
    public final RequestRepository requestRepository;

    public final UserRepository userRepository;

    public CreateRequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;

    }
    @Autowired
    private UserSession userSession;
    @Override
    public Requests createRequests(Long assignToId, String description, MultipartFile file, MultipartFile imageFile, Requests.Priority priority,
                                   Requests.Status status, String title, String videoLink) {
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
}
