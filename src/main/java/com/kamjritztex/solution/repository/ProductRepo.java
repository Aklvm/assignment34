package com.kamjritztex.solution.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kamjritztex.solution.entity.ProductEntity;

@Repository
public interface ProductRepo  extends JpaRepository<ProductEntity,Integer>{
  
      @Query("SELECT p FROM ProductEntity p WHERE p.available = true ORDER BY p.rating DESC, p.noOfPurchase DESC")
      List<ProductEntity> findTopByOrderByRatingDescNoOfPurchaseDesc();

      @Query("SELECT p FROM ProductEntity p WHERE p.bestSeller =true and p.available = true  ORDER BY p.rating DESC")
      List<ProductEntity> findBestSellerProduct();
} 