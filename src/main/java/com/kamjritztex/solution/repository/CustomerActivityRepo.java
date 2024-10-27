package com.kamjritztex.solution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.solution.entity.CustomerActivity;

@Repository
public interface CustomerActivityRepo extends JpaRepository<CustomerActivity,Integer>{

    public List<CustomerActivity> findByProcessed(boolean flase);

    
} 