package com.api.requesttracker.repository;

import com.api.requesttracker.dto.RequestAnalysisDTO;
import com.api.requesttracker.entity.Requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Requests, Long> {
    @Query(value = "SELECT * FROM requesttrack.requests where requestid = :id",nativeQuery = true)
    Requests getRequestById(Long id);

}