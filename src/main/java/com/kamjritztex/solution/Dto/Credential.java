package com.kamjritztex.solution.Dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Component
@Getter
@Setter
public class Credential {
    @NonNull 
    private String emailId;
    @NonNull
    private String password;
    
}
