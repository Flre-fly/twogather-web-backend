package com.twogather.twogatherwebbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BusinessHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="business_hour_id")
    private Long businessHourId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private Boolean isOpen;
    private Boolean hasBreakTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;

    public void update(LocalTime startTime, LocalTime endTime, DayOfWeek dayOfWeek, Boolean isOpen,
                       Boolean hasBreakTime, LocalTime breakStartTime, LocalTime breakEndTime){
        if(startTime!=null){
            this.startTime = startTime;
        }
        if(endTime!=null){
            this.endTime = endTime;
        }
        if(dayOfWeek!=null){
            this.dayOfWeek = dayOfWeek;
        }
        if(breakStartTime!=null){
            this.breakStartTime = breakStartTime;
        }
        if(breakEndTime!=null){
            this.breakEndTime = breakEndTime;
        }
        if(isOpen!=null){
            this.isOpen = isOpen;
        }
        if(hasBreakTime!=null){
            this.hasBreakTime = hasBreakTime;
        }

    }
    public void addStore(Store store){
        if (this.store != null) {
            this.store.getBusinessHourList().remove(this);
        }
        this.store = store;
        store.addBusinessHour(this);
    }
}
