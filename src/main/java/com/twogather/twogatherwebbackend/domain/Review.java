package com.twogather.twogatherwebbackend.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Consumer reviewer;

    //이거 string으로 해도되는건가
    private String content;
    private Double score;
}
