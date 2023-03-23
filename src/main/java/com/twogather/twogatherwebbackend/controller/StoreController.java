package com.twogather.twogatherwebbackend.controller;

import com.twogather.twogatherwebbackend.dto.store.StoreSaveRequest;
import com.twogather.twogatherwebbackend.dto.store.StoreUpdateRequest;
import com.twogather.twogatherwebbackend.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final StoreSaveRequest storeSaveRequest) {
        storeService.save(storeSaveRequest);

        return ResponseEntity.created(URI.create("/api/stores/")).build();
    }
    @PutMapping("/{storeId}")
    public ResponseEntity<Void> update(@PathVariable Long storeId, @RequestBody @Valid StoreUpdateRequest storeUpdateRequest) {
        storeService.update(storeId, storeUpdateRequest);

        return ResponseEntity.created(URI.create("/api/stores/")).build();
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> delete(@PathVariable Long storeId) {
        storeService.delete(storeId);

        return ResponseEntity.noContent().build();
    }
}
