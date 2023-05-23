package com.twogather.twogatherwebbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("store_owner")
public class StoreOwner extends Member {
    @OneToMany(mappedBy = "owner")
    private List<Store> storeList = new ArrayList<>();

    private String businessNumber;
    private String businessName;
    private LocalDate businessStartDate;

    public StoreOwner(String username, String email, String loginPw, String name, String businessNumber, String businessName, LocalDate businessStartDate, AuthenticationType authenticationType, boolean isActive) {
        super(username, email, loginPw, name, authenticationType, isActive);
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.businessStartDate = businessStartDate;
    }


}
