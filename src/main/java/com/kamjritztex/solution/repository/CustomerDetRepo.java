package com.kamjritztex.solution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.solution.entity.CustomerDet;

@Repository
public interface CustomerDetRepo extends JpaRepository<CustomerDet,Integer>{

    
} 
