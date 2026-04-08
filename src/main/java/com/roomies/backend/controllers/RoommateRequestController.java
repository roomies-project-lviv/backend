package com.roomies.backend.controllers;

import com.roomies.backend.dto.RoommateRequestCreateDto;
import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.dto.filters.RoommateFilterDto;
import com.roomies.backend.services.RoommateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.roomies.backend.dto.filters.RoommateFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roommate-requests")
public class RoommateRequestController {

    @Autowired
    private RoommateRequestService requestService;

    @GetMapping
    public ResponseEntity<List<RoommateRequestDto>> getAllActiveRequests() {
        return ResponseEntity.ok(requestService.getAllActiveRequests());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<RoommateRequestDto>> searchRequests(
            @RequestBody(required = false) RoommateFilterDto filterDto, 
            Pageable pageable) {
        
        if (filterDto == null) filterDto = new RoommateFilterDto();
        return ResponseEntity.ok(requestService.getAllActiveRequests(filterDto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoommateRequestDto> getRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @PostMapping
    public ResponseEntity<RoommateRequestDto> createRequest(@RequestBody RoommateRequestCreateDto createDto) {
        return new ResponseEntity<>(requestService.createRequest(createDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoommateRequestDto> updateRequest(@PathVariable UUID id, @RequestBody RoommateRequestCreateDto updateDto) {
        return ResponseEntity.ok(requestService.updateRequest(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    
}