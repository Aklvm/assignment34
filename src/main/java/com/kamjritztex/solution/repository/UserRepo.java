package com.kamjritztex.solution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.solution.entity.UserCredential;

@Repository
public interface UserRepo extends JpaRepository<UserCredential,String>{
    
}
