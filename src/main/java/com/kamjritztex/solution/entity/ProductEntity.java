package com.kamjritztex.solution.entity;

import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Component
public class ProductEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Nonnull
    private String tittle;
    @Nonnull
    private String description;
    @Nonnull
    private int price;
    @Nonnull
    private boolean available;
    @Nonnull
    private boolean bestSeller;
    @Nonnull
    private int rating ;
    @Nonnull
    private Long noOfPurchase;
}
