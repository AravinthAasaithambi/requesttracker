package com.api.requesttracker.services;

import com.api.requesttracker.entity.Requests;

import java.util.Optional;

public interface UpdateRequestsDetailsService {
    Optional<Requests> findById(Long id);

    Requests getRequestById(Long requestid);

    Requests rejectRequest(Long requestId);

    String assignRequest(Long userId, Long requestId);

    Requests changeStatus(Long id, Requests.Status status);

    Requests changePriority(Long id, Requests.Priority priority);
}
