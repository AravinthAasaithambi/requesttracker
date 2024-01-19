package com.api.requesttracker.services;

import com.api.requesttracker.entity.Requests;

import java.util.List;

public interface RequestAssignedService {
    List<Requests> assingnedRequestForUser();

    List<Requests> assingnedRequestByUser();
}
