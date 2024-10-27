package com.kamjritztex.solution.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kamjritztex.solution.Dto.Message;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired 
    Message message;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Message> handleCustomException(CustomException cx){
            message.setResult("failed  with system caught exception");
            message.setMessage(cx);
            return ResponseEntity.internalServerError().body(message);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleGeneralException(Exception ex){
            message.setResult("Unexpected exception occurred");
            message.setMessage(ex.getMessage());
            return ResponseEntity.internalServerError().body(message);

    }
}
