package com.twogather.twogatherwebbackend.controller;

import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourResponse;
import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourSaveRequest;
import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourUpdateRequest;
import com.twogather.twogatherwebbackend.dto.store.StoreResponse;
import com.twogather.twogatherwebbackend.service.BusinessHourService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/business-hour")
@RequiredArgsConstructor
public class BusinessHourController {
    private final BusinessHourService businessHourService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody @Valid final BusinessHourSaveRequest businessHourSaveRequest) {
        BusinessHourResponse data = businessHourService.save(businessHourSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(data));
    }
    @PutMapping("/{businessHourId}")
    public ResponseEntity<Response> update(@PathVariable Long businessHourId, @RequestBody @Valid BusinessHourUpdateRequest businessHourUpdateRequest) {
        BusinessHourResponse data = businessHourService.update(businessHourId, businessHourUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new Response(data));
    }

    @DeleteMapping("/{businessHourId}")
    public ResponseEntity<Void> delete(@PathVariable Long businessHourId) {
        businessHourService.delete(businessHourId);

        return ResponseEntity.noContent().build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    private static class Response {
        private BusinessHourResponse data;
    }
}