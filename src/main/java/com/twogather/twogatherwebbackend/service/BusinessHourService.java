package com.twogather.twogatherwebbackend.service;

import com.twogather.twogatherwebbackend.domain.BusinessHour;
import com.twogather.twogatherwebbackend.domain.Store;
import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourResponse;
import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourSaveRequest;
import com.twogather.twogatherwebbackend.dto.businesshour.BusinessHourUpdateRequest;
import com.twogather.twogatherwebbackend.exception.BusinessHourException;
import com.twogather.twogatherwebbackend.exception.StoreException;
import com.twogather.twogatherwebbackend.repository.BusinessHourRepository;
import com.twogather.twogatherwebbackend.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessHourService {
    private final BusinessHourRepository businessHourRepository;
    private final StoreRepository storeRepository;

    public List<BusinessHourResponse> findBusinessHoursByStoreId(Long storeId) {
        List<BusinessHour> businessHours = businessHourRepository.findByStoreStoreId(storeId);
        if (businessHours.isEmpty()) {
            throw new BusinessHourException(BusinessHourException.BusinessHourErrorCode.NO_SUCH_BUSINESS_HOUR_BY_STORE_ID);
        }
        ArrayList<BusinessHourResponse> responses = new ArrayList<>();
        for (BusinessHour businessHour: businessHours) {
            responses.add(toBusinessHourResponse(businessHour));
        }
        return responses;
    }

    public BusinessHourResponse save(@Valid @RequestBody BusinessHourSaveRequest request){
        validateDuplicateDayOfWeek(request.getStoreId(), request.getDayOfWeek());
        Store store = findByStoreIdReturnStore(request.getStoreId());
        BusinessHour businessHour = new BusinessHour(store,request.getStartTime(), request.getEndTime(), request.getDayOfWeek(), request.isOpen());
        BusinessHour savedBusinessHour = businessHourRepository.save(businessHour);
        return toBusinessHourResponse(savedBusinessHour);
    }
    public BusinessHourResponse update(Long id, @Valid @RequestBody BusinessHourUpdateRequest request){
        BusinessHour businessHour = findBusinessHour(id);
        businessHour.updateEndTime(request.getEndTime());
        businessHour.updateStartTime(request.getStartTime());
        businessHour.updateDayOfWeek(request.getDayOfWeek());
        businessHour.updateIsClosed(request.isOpen());

        return toBusinessHourResponse(businessHour);
    }
    public void delete(Long businessHourId){
        validateBusinessHourId(businessHourId);
        businessHourRepository.deleteById(businessHourId);
    }

    private Store findByStoreIdReturnStore(Long id){
        Optional<Store> store = storeRepository.findById(id);
        store.orElseThrow(()->new StoreException(StoreException.StoreErrorCode.STORE_NOT_FOUND));
        return store.get();
    }
    private void validateBusinessHourId(Long businessHourId){
        businessHourRepository.findById(businessHourId).orElseThrow(
                () -> new BusinessHourException(BusinessHourException.BusinessHourErrorCode.NO_SUCH_BUSINESS_HOUR_BY_BUSINESS_HOUR_ID)
        );
    }
    private BusinessHour findBusinessHour(Long businessHourId){
        Optional<BusinessHour> businessHour = businessHourRepository.findById(businessHourId);
        businessHour.orElseThrow(
                () -> new BusinessHourException(BusinessHourException.BusinessHourErrorCode.NO_SUCH_BUSINESS_HOUR_BY_BUSINESS_HOUR_ID)
        );
        return businessHour.get();
    }
    private void validateDuplicateDayOfWeek(Long storeId, DayOfWeek dayOfWeek) {
        Optional<BusinessHour> businessHourOptional = businessHourRepository.findByStoreStoreIdAndDayOfWeek(storeId, dayOfWeek);
        if (businessHourOptional.isPresent()) {
            throw new BusinessHourException(BusinessHourException.BusinessHourErrorCode.DUPLICATE_DAY_OF_WEEK);
        }
    }
    private BusinessHourResponse toBusinessHourResponse(BusinessHour businessHour){
        return new BusinessHourResponse(businessHour.getBusinessHourId(), businessHour.getStore().getStoreId(),
                businessHour.getStartTime(), businessHour.getEndTime(), businessHour.getDayOfWeek(), businessHour.isOpen());

    }
}