package com.kamjritztex.solution.Dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Component
public class Message {

    private String result;
    private Object message;
    
}
