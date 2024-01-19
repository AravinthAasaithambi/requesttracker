package com.api.requesttracker.services;

import com.api.requesttracker.entity.Requests;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CreateRequestService {

    /*This method specifies to create one single Request*/
    Requests createRequests(Long assignToId, String description, MultipartFile file, MultipartFile imageFile,
                            Requests.Priority priority, Requests.Status status, String title, String videoLink);

}
