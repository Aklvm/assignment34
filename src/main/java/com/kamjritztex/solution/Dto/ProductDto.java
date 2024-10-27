package com.kamjritztex.solution.Dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProductDto {
    private int id;
    private String tittle;
    private String description;
    private int price;
    private boolean available;
    private boolean bestSeller;
    private int rating ;
    private Long noOfPurchase;



}
